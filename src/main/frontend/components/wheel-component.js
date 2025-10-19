class WheelComponent extends HTMLElement {
    static get observedAttributes() {
        return ["segments"];
    }

    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
        this.canvas = document.createElement("canvas");
        this.canvas.width = 400;
        this.canvas.height = 400;
        this.shadowRoot.appendChild(this.canvas);
        this.ctx = this.canvas.getContext("2d");

        this.segment = [];
        this.colors = ["#000000", "#ED7465", "#9966CC", "#CD5C5C", "#918151", "#ACE1AF", "#00A86B", "#C9A0DC", "#C8A2C8", "#987654", "#9AB973", "#CCCCFF", "#FFE5B4",
                       "#ED7465", "#9966CC", "#000000",  "#CD5C5C", "#918151", "#00A86B", "#C9A0DC", "#ACE1AF", "#C8A2C8", "#987654", "#CCCCFF"];

        this.rotation = 0;
        this.isSpinning = false;
    }

    attributeChangedCallback(name, oldValue, newValue) {
        if(name === "segments" && newValue) {
            try {
                this.segment = JSON.parse(newValue);
                this.arc = 2 * Math.PI / this.segment.length;
                this.drawWheel();
            } catch (e) {
                console.error("Invalid segment data", e);
            }
        }
    }

    drawWheel() {
        let ctx = this.ctx;
        let radius = this.canvas.width / 2;
        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        for(let i = 0; i < this.segment.length; i++) {
            let angle = this.rotation + i * this.arc - Math.PI / 2;

            this.ctx.beginPath();
            this.ctx.fillStyle = this.colors[i % this.colors.length];
            this.ctx.moveTo(radius, radius);
            this.ctx.arc(radius, radius, radius, angle, angle + this.arc);
            this.ctx.fill();

            this.ctx.save();
            this.ctx.translate(radius, radius);
            this.ctx.rotate(angle + this.arc / 2);
            this.ctx.fillStyle = "white";
            this.ctx.textAlign = "right";
            this.ctx.font = "14px Comic Sans MS, sans-serif";
            this.ctx.fillText(this.segment[i], radius - 10, 5);
            this.ctx.restore();
        }
    }

    spinTo(targetIndex) {
        if(this.isSpinning) return;
        this.isSpinning = true;

        let arc = 2 * Math.PI / this.segment.length;
        let baseAngle = 0;

        let targetAngle = baseAngle + (targetIndex * arc) + (arc / 2);

        let randomOffset = (Math.random() - 0.5) * arc * 0.1;
        targetAngle += randomOffset;

        let extraRotations = 5 * 2 * Math.PI; //5 giri completi
        let finalRotation = extraRotations - targetAngle;

        let start = null;
        const duration = 4000; //4s

        const animate = (timestamp) => {
            if(!start) start = timestamp;
            let progress = (timestamp - start) / duration;
            if(progress > 1) progress = 1;

            let eased = 1 - Math.pow(1 - progress, 3);
            this.rotation = finalRotation * eased;

            this.drawWheel();

            if(progress < 1) {
                requestAnimationFrame(animate);
            } else {
                this.isSpinning = false;
                this.dispatchEvent(new CustomEvent("wheel-stopped", {
                    detail: this.segment[targetIndex],
                    bubbles: true
                }));
            }
        };

        requestAnimationFrame(animate);
    }
}

customElements.define("wheel-component", WheelComponent);