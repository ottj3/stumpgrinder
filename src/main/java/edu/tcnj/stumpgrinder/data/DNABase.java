package edu.tcnj.stumpgrinder.data;

public enum DNABase {
    A(0), G(1), T(2), C(3);

    public int value;

    private DNABase(int value) {
        this.value = value;
    }

}
