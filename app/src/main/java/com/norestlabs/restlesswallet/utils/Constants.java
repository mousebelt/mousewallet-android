package com.norestlabs.restlesswallet.utils;

import com.norestlabs.restlesswallet.models.wallet.EthereumToken;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String SHAPESHIFT_URL = "https://shapeshift.io/";
    public static final String ETHERCHAIN_URL = "https://www.etherchain.org/api/";
    public static final String BTCFEE_URL = "https://bitcoinfees.earn.com/api/v1/fees/";
    public static final String COINMARKET_URL = "https://api.coinmarketcap.com/v2/";

    public static final int CONNECT_TIMEOUT = 30;
    public static final int SPLASH_DURATION = 0;
    public static final int PIN_DURATION = 100;
    public static final int PIN_LENGTH = 6;
    public static final int MNEMONIC_SIZE = 12;
    public static final boolean USE_SYSTEM_SERVICE_TO_REMOVE_PREFERENCE = true;

    public static final List<EthereumToken> ETH_TOKENS = new ArrayList<EthereumToken>() {{
        add(new EthereumToken("Ethereum", "ETH", "", 18));
        add(new EthereumToken("TRONix", "TRON", "0xf230b790e05390fc8295f4d3f60332c93bed42e2", 6));
        add(new EthereumToken("VeChain", "VEN", "0xd850942ef8811f2a866692a623011bde52a462c1", 18));
        add(new EthereumToken("Binance Coin", "BNB", "0xB8c77482e45F1F44dE1745F52C74426C631bDD52", 18));
        add(new EthereumToken("OmiseGO", "OMG", "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07", 18));
        add(new EthereumToken("Zilliqa", "ZIL", "0x05f4a42e251f2d52b8ed15e9fedaacfcef1fad27", 12));
        add(new EthereumToken("Aeternity", "AE", "0x5ca9a71b1d01849c0a95490cc00559717fcf0d1d", 18));
        add(new EthereumToken("ZRX", "ZRX", "0xe41d2489571d322189246dafa5ebde1f4699f498", 18));
        add(new EthereumToken("Bytom", "BTM", "0xcb97e65f07da24d46bcdd078ebebd7c6e6e3d750", 8));
        add(new EthereumToken("RHOC", "RHOC", "0x168296bb09e24a88805cb9c33356536b980d3fc5", 8));
        add(new EthereumToken("REP", "REP", "0xe94327d07fc17907b4db788e5adf2ed424addff6", 18));
        add(new EthereumToken("Maker", "MKR", "0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2", 18));
        add(new EthereumToken("Populous", "PPT", "0xd4fa1460f537bb9085d22c7bccb5dd450ef28e3a", 8));
        add(new EthereumToken("Golem", "GNT", "0xa74476443119A942dE498590Fe1f2454d7D4aC0d", 18));
        add(new EthereumToken("IOSToken", "IOST", "0xfa1a856cfa3409cfa145fa4e20eb270df3eb21ab", 18));
        add(new EthereumToken("StatusNetwork", "SNT", "0x744d70fdbe2ba4cf95131626614a1763df805b9e", 18));
        add(new EthereumToken("Walton", "WTC", "0xb7cb1c96db6b22b0d3d9536e0108d062bd488f74", 18));
        add(new EthereumToken("AION", "AION", "0x4CEdA7906a5Ed2179785Cd3A40A69ee8bc99C466", 8));
        add(new EthereumToken("DigixDAO", "DGD", "0xe0b7927c4af23765cb51314a0e0521a9645f0e2a", 9));
        add(new EthereumToken("Nebulas", "NAS", "0x5d65D971895Edc438f465c17DB6992698a52318D", 18));
        add(new EthereumToken("Loopring", "LRC", "0xef68e7c694f40c8202821edf525de3782458639f", 18));
        add(new EthereumToken("BAT", "BAT", "0x0d8775f648430679a709e98d2b0cb6250d2887ef", 18));
        add(new EthereumToken("ELF", "ELF", "0xbf2179859fc6d5bee9bf9158632dc51678a4100e", 18));
        add(new EthereumToken("Dentacoin", "DCN", "0x08d32b0da63e2C3bcF8019c9c5d849d7a9d791e6", 0));
        add(new EthereumToken("Loom", "LOOM", "0xa4e8c3ec456107ea67d3075bf9e3df3a75823db0", 18));
        add(new EthereumToken("KyberNetwork", "KNC", "0xdd974d5c2e2928dea5f71b9825b8b646686bd200", 18));
        add(new EthereumToken("Ethos", "ETHOS", "0x5af2be193a6abca9c8817001f45744777db30756", 8));
        add(new EthereumToken("Substratum", "SUB", "0x12480e24eb5bec1a9d4369cab6a80cad3c0a377a", 2));
        add(new EthereumToken("Bancor", "BNT", "0x1f573d6fb3f13d689ff844b4ce37794d79a7ff1c", 18));
        add(new EthereumToken("Polymath", "POLY", "0x9992ec3cf6a55b00978cddf2b27bc6882d88d1ec", 18));
        add(new EthereumToken("QASH", "QASH", "0x618e75ac90b12c6049ba3b27f5d5f8651b0037f6", 6));
        add(new EthereumToken("FunFair", "FUN", "0x419d0d8bdd9af5e606ae2232ed285aff190e711b", 8));
        add(new EthereumToken("Fusion", "FSN", "0xd0352a019e9ab9d757776f532377aaebd36fd541", 18));
        add(new EthereumToken("Dragon", "DRGN", "0x419c4db4b9e25d6db2ad9691ccb832c8d9fda05e", 18));
        add(new EthereumToken("Cortex Coin", "CTXC", "0xea11755ae41d889ceec39a63e6ff75a02bc1c00d", 18));
        add(new EthereumToken("Enigma", "ENG", "0xf0ee6b27b759c9893ce4f094b49ad28fd15a23e4", 8));
        add(new EthereumToken("HuobiToken", "HT", "0x6f259637dcd74c767781e37bc6133cd6a68aa161", 18));
        add(new EthereumToken("Nexo", "NEXO", "0xb62132e35a6c13ee1ee0f84dc5d40bad8d815206", 18));
        add(new EthereumToken("Storm", "STORM", "0xd0a4b8946cb52f0661273bfbc6fd0e0c75fc6433", 18));
        add(new EthereumToken("Nuls", "NULS", "0xb91318f35bdb262e9423bc7c7c2a3a93dd93c92c", 18));
        add(new EthereumToken("Salt", "SALT", "0x4156D3342D5c385a87D264F90653733592000581", 8));
        add(new EthereumToken("ICON", "ICX", "0xb5a5f22694352c15b00323844ad545abb2b11028", 18));
    }};
}
