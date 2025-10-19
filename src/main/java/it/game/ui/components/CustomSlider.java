package it.game.ui.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;

@Tag("custom-slider")
@JsModule("./components/custom-slider.ts")
public class CustomSlider extends Component {

    public CustomSlider() {}

    public void setMin(int min) {
        getElement().setProperty("min", min);
    }

    public void setMax(int max) {
        getElement().setProperty("max", max);
    }

    public void setValue(int value) {
        getElement().setProperty("value", value);
    }

    public int getValue() {
        return getElement().getProperty("value", 0);
    }

    @DomEvent("value-changed")
    public static class ValueChangeEvent extends ComponentEvent<CustomSlider> {
        private final int value;

        public ValueChangeEvent(CustomSlider source, boolean fromClient, @EventData("event.detail.value") int value) {
            super(source, fromClient);
            this.value = value;
        }

        public int getValue() { return value; }
    }

    public Registration addValueChangeListener(ComponentEventListener<ValueChangeEvent> listener) {
        return addListener(ValueChangeEvent.class, listener);
    }
}
