package Model.type;

import Model.value.StringValue;

public class StringType implements Type{
    @Override
    public boolean equals(Object other) {
        return other instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public StringValue defaultValue() {
        return new StringValue("");
    }
}
