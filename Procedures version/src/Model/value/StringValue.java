package Model.value;

import Model.type.StringType;
import Model.type.Type;

public class StringValue implements Value{

    private final String val;

    public StringValue(String value){ this.val = value; }

    public String getValue() { return val; }

    @Override
    public Type getType() { return new StringType(); }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StringValue;
    }
}
