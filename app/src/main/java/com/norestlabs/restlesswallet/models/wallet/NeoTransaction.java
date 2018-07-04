package com.norestlabs.restlesswallet.models.wallet;

import java.util.List;

public class NeoTransaction extends Transaction {
    public String txid;
    public int size;
    public String type;
    public int version;
    public List<Vin> vin;
    public List<Vout> vout;
    public int sys_fee;
    public int net_fee;
    public String blockhash;
    public String confirmations;
    public String blocktime;

    public class Vin {
        public String txid;
        public int vout;
        public Vout address;
    }

    public class Vout {
        public int n;
        public String asset;
        public int value;
        public String address;
    }
}
