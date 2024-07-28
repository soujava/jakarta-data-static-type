package os.expert.demo.helidon;


import jakarta.json.bind.annotation.JsonbVisibility;

@JsonbVisibility(FieldAccessStrategy.class)
public class GasEngine extends Engine {

    public GasEngine() {
    }

    public GasEngine(int horsepower) {
        super(horsepower);
    }

    @Override
    public String getFuelType() {
        return "Gasoline";
    }


    @Override
    public String toString() {
        return "GasEngine{" +
                "horsepower=" + horsepower +
                '}';
    }
}