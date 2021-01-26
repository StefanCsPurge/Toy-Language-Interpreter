package Model.value;

import Model.type.RefType;
import Model.type.Type;

public class RefValue implements Value{
    private final int address;
    private final Type locationType;

    public int getAddr() { return address; }

    public RefValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    public Type getLocationType() { return locationType; }

    @Override
    public Type getType() { return new RefType(locationType); }

    @Override
    public String toString() {
        return "(" + address + "," + locationType.toString() + ")";
    }
}
