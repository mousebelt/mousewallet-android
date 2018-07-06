package com.norestlabs.restlesswallet.models.wallet;

import java.util.List;

public class LitecoinTransaction extends Transaction {
    public String txid;
    public String hash;
    public int version;
    public int size;
    public int vsize;
    public int locktime;
    public List<Vin> vin;
    public List<Vout> vout;
    public String hex;
    public String blockhash;
    public long confirmations;
    public long time;
    public long blocktime;

    public class Vin {
        public String txid;
        public int vout;
        public ScriptSig scriptSig;
        public String sequence;
        public Vout address;
    }

    public class ScriptSig {
        public String asm;
        public String hex;
        public int reqSigs;
        public String type;
        public List<String> addresses;
    }

    public class Vout {
        public double value;
        public int n;
        public ScriptSig scriptPubKey;
    }
}
