package Model.value;

import Model.type.Type;
import Model.type.BoolType;

public class BoolValue implements Value{
    private final boolean val;

    public BoolValue(boolean value) { this.val = value; }

    public boolean getValue() { return val; }

    @Override
    public Type getType() { return new BoolType(); }

    @Override
    public String toString() { return String.valueOf(val);}

    @Override
    public boolean equals(Object other) {
        return other instanceof BoolValue;
    }
}
