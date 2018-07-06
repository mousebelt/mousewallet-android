package com.norestlabs.restlesswallet.models.wallet;

import java.util.Date;
import java.util.List;

public class StellarTransaction extends Transaction {
    public Link _links;
    public String id;
    public String paging_token;
    public String hash;
    public String ledger;
    public Date created_at;
    public String source_account;
    public String source_account_sequence;
    public double fee_paid;
    public int operation_count;
    public String envelope_xdr;
    public String result_meta_xdr;
    public String fee_meta_xdr;
    public String memo_type;
    public List<String> signatures;

    public String getId() {
        return id;
    }

    public String getLedger() {
        return ledger;
    }

    public int getOperationCount() {
        return operation_count;
    }

    public class Link {
        public HRef self;
        public HRef account;
        public HRef ledger;
        public HRef operations;
        public HRef effects;
        public HRef precedes;
        public HRef succeeds;
    }

    public class HRef {
        public String href;
        public boolean templated;
    }
}
