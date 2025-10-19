import { html, css, LitElement } from 'lit';
import { customElement, property } from 'lit/decorators.js';

@customElement('custom-slider')
export class CustomSlider extends LitElement {

    @property({ type: Number }) min = 1;
    @property({ type: Number }) max = 10;
    @property({ type: Number }) value = 5;
    @property({ type: Number }) step = 1;

    private isDragging = false;

    static styles = css`
        .slider-container {
            position: relative;
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
            
        input[type=range] {
            -webkit-appearance: none;
            width: 90%;
            height: 10px;
            background: cornflowerblue;
            border-radius: 5px;
            outline: none;
            cursor: pointer;
        }
        
        .track {
            position: absolute;
            width: 90%;
            height: 10px;
            background: lightgray;
            border-radius: 5px;
            overflow: hidden;
        }
        
        .fill {
            position: absolute;
            height: 100%;
            background: linear-gradient(110deg, #00008B, #9ac4f5);
            border-radius: 5px 0 0 5px;
            top: 0;
            left: 0;
        }
        
        .thumb {
            position: absolute;
            top: 50%;
            transform: translate(-50%, -50%);
            width: 38px;
            height: 38px;
            background: dodgerblue;
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Comic Neue', cursive;
            font-size: 18px;
            font-weight: bold;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
            transition: transform 0.1s ease-in-out;
            cursor: grab;
            user-select: none;
            touch-action: none;
        }
        
        .thumb:hover {
            transform: translate(-50%, -50%) scale(1.1);
        }
        
        .thumb.dragging {
            cursor: grabbing;
            transform: translate(-50%, -50%) scale(1.2);
        }
    `;

    firstUpdated() {
        this.setupDragEvents();
    }

    setupDragEvents() {
        const thumb = this.shadowRoot?.querySelector('.thumb') as HTMLElement;
        if(!thumb) return;

        thumb.addEventListener('mousedown', this.startDrag.bind(this));
        thumb.addEventListener('touchstart', this.startDrag.bind(this));

        thumb.addEventListener('dragstart', (e) => e.preventDefault());
    }

    startDrag(event: MouseEvent | TouchEvent) {
        event.preventDefault();
        this.isDragging = true;

        const thumb = this.shadowRoot?.querySelector('.thumb') as HTMLElement;
        if(thumb) {
            thumb.classList.add('dragging');
        }

        if(event instanceof MouseEvent) {
            document.addEventListener('mousemove', this.handleDrag);
            document.addEventListener('mouseup', this.stopDrag);
        } else {
            document.addEventListener('touchmove', this.handleDrag);
            document.addEventListener('touchend', this.stopDrag);
        }
    }

    handleDrag = (event: MouseEvent | TouchEvent) => {
        if(!this.isDragging) return;

        const sliderContainer = this.shadowRoot?.querySelector('.slider-container') as HTMLElement;
        if(!sliderContainer) return;

        const rect = sliderContainer.getBoundingClientRect();
        const sliderWidth = rect.width * 0.9;
        const sliderStart = rect.left + (rect.width * 0.05);

        let clientX: number;
        if(event instanceof MouseEvent) {
            clientX = event.clientX;
        } else {
            clientX =event.touches[0].clientX;
        }

        let position = clientX - sliderStart;
        position = Math.max(0, Math.min(position, sliderWidth));

        const percentage = position / sliderWidth;
        const newValue = Math.round(percentage * (this.max - this.min) + this.min);

        const steppedValue = Math.round((newValue - this.min) / this.step) * this.step + this.min;
        const finalValue = Math.max(this.min, Math.min(steppedValue, this.max));

        if(finalValue !== this.value) {
            this.value = finalValue;
            this.dispatchEvent(new CustomEvent('value-changed', { detail: { value: this.value } }));
            this.requestUpdate();
        }
    }

    stopDrag = () => {
        this.isDragging = false;

        const thumb = this.shadowRoot?.querySelector('.thumb') as HTMLElement;
        if(thumb) {
            thumb.classList.remove('dragging');
        }

        document.removeEventListener('mousemove', this.handleDrag);
        document.removeEventListener('mouseup', this.stopDrag);
        document.removeEventListener('touchmove', this.handleDrag);
        document.removeEventListener('touchend', this.stopDrag);
    }

    onInput(event: Event) {
        const target = event.target as HTMLInputElement;
        this.value = Number(target.value);
        this.dispatchEvent(new CustomEvent('value-changed', { detail: { value: this.value } }));
        this.requestUpdate();
    }

    render() {
        const percent = ((this.value - this.min) / (this.max - this.min)) * 100;

        return html`
        <div class="slider-container">
            <div class="track">
                <div class="fill" style="width: ${percent}%;"></div>
            </div>
            <div class="thumb" style="left: calc(${percent}% * 0.9 + 5%);">
                ${this.value}
            </div>
            <input type="range"
                   min="${this.min}"
                   max="${this.max}"
                   step="${this.step}"
                   .value="${this.value}"
                   @input="${this.onInput}"
            />
        </div>
        `;
    }
}