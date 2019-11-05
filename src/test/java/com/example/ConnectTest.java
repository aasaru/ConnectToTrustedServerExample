package com.example;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConnectTest {

    // I obtained the google.com cert like this:
    // % echo | openssl s_client -servername google.com -connect google.com:443 > certificate.crt

    static final String GOOGLE_CERT = "-----BEGIN CERTIFICATE-----\n"
         + "MIIJRTCCCC2gAwIBAgIRAOqrc47MKQZ1AgAAAABH2REwDQYJKoZIhvcNAQELBQAw\n"
         + "QjELMAkGA1UEBhMCVVMxHjAcBgNVBAoTFUdvb2dsZSBUcnVzdCBTZXJ2aWNlczET\n"
         + "MBEGA1UEAxMKR1RTIENBIDFPMTAeFw0xOTEwMTAyMTAyMjhaFw0yMDAxMDIyMTAy\n"
         + "MjhaMGYxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQH\n"
         + "Ew1Nb3VudGFpbiBWaWV3MRMwEQYDVQQKEwpHb29nbGUgTExDMRUwEwYDVQQDDAwq\n"
         + "Lmdvb2dsZS5jb20wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAASQGSFNZeb85+EY\n"
         + "HSJTKF+w+U3aVOBYE5N2tU4DTzaEfJozQwEdxjjHflOLDowLzMUFQHuc0zGuBN5L\n"
         + "MEkhPCyCo4IG2zCCBtcwDgYDVR0PAQH/BAQDAgeAMBMGA1UdJQQMMAoGCCsGAQUF\n"
         + "BwMBMAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYEFP68VbeQZB5338zH/1Vw2jQ9fyHZ\n"
         + "MB8GA1UdIwQYMBaAFJjR+G4Q68+b7GCfGJAboOt9Cf0rMGQGCCsGAQUFBwEBBFgw\n"
         + "VjAnBggrBgEFBQcwAYYbaHR0cDovL29jc3AucGtpLmdvb2cvZ3RzMW8xMCsGCCsG\n"
         + "AQUFBzAChh9odHRwOi8vcGtpLmdvb2cvZ3NyMi9HVFMxTzEuY3J0MIIEnQYDVR0R\n"
         + "BIIElDCCBJCCDCouZ29vZ2xlLmNvbYINKi5hbmRyb2lkLmNvbYIWKi5hcHBlbmdp\n"
         + "bmUuZ29vZ2xlLmNvbYISKi5jbG91ZC5nb29nbGUuY29tghgqLmNyb3dkc291cmNl\n"
         + "Lmdvb2dsZS5jb22CBiouZy5jb4IOKi5nY3AuZ3Z0Mi5jb22CESouZ2NwY2RuLmd2\n"
         + "dDEuY29tggoqLmdncGh0LmNugg4qLmdrZWNuYXBwcy5jboIWKi5nb29nbGUtYW5h\n"
         + "bHl0aWNzLmNvbYILKi5nb29nbGUuY2GCCyouZ29vZ2xlLmNsgg4qLmdvb2dsZS5j\n"
         + "by5pboIOKi5nb29nbGUuY28uanCCDiouZ29vZ2xlLmNvLnVrgg8qLmdvb2dsZS5j\n"
         + "b20uYXKCDyouZ29vZ2xlLmNvbS5hdYIPKi5nb29nbGUuY29tLmJygg8qLmdvb2ds\n"
         + "ZS5jb20uY2+CDyouZ29vZ2xlLmNvbS5teIIPKi5nb29nbGUuY29tLnRygg8qLmdv\n"
         + "b2dsZS5jb20udm6CCyouZ29vZ2xlLmRlggsqLmdvb2dsZS5lc4ILKi5nb29nbGUu\n"
         + "ZnKCCyouZ29vZ2xlLmh1ggsqLmdvb2dsZS5pdIILKi5nb29nbGUubmyCCyouZ29v\n"
         + "Z2xlLnBsggsqLmdvb2dsZS5wdIISKi5nb29nbGVhZGFwaXMuY29tgg8qLmdvb2ds\n"
         + "ZWFwaXMuY26CESouZ29vZ2xlY25hcHBzLmNughQqLmdvb2dsZWNvbW1lcmNlLmNv\n"
         + "bYIRKi5nb29nbGV2aWRlby5jb22CDCouZ3N0YXRpYy5jboINKi5nc3RhdGljLmNv\n"
         + "bYISKi5nc3RhdGljY25hcHBzLmNuggoqLmd2dDEuY29tggoqLmd2dDIuY29tghQq\n"
         + "Lm1ldHJpYy5nc3RhdGljLmNvbYIMKi51cmNoaW4uY29tghAqLnVybC5nb29nbGUu\n"
         + "Y29tghMqLndlYXIuZ2tlY25hcHBzLmNughYqLnlvdXR1YmUtbm9jb29raWUuY29t\n"
         + "gg0qLnlvdXR1YmUuY29tghYqLnlvdXR1YmVlZHVjYXRpb24uY29tghEqLnlvdXR1\n"
         + "YmVraWRzLmNvbYIHKi55dC5iZYILKi55dGltZy5jb22CGmFuZHJvaWQuY2xpZW50\n"
         + "cy5nb29nbGUuY29tggthbmRyb2lkLmNvbYIbZGV2ZWxvcGVyLmFuZHJvaWQuZ29v\n"
         + "Z2xlLmNughxkZXZlbG9wZXJzLmFuZHJvaWQuZ29vZ2xlLmNuggRnLmNvgghnZ3Bo\n"
         + "dC5jboIMZ2tlY25hcHBzLmNuggZnb28uZ2yCFGdvb2dsZS1hbmFseXRpY3MuY29t\n"
         + "ggpnb29nbGUuY29tgg9nb29nbGVjbmFwcHMuY26CEmdvb2dsZWNvbW1lcmNlLmNv\n"
         + "bYIYc291cmNlLmFuZHJvaWQuZ29vZ2xlLmNuggp1cmNoaW4uY29tggp3d3cuZ29v\n"
         + "Lmdsggh5b3V0dS5iZYILeW91dHViZS5jb22CFHlvdXR1YmVlZHVjYXRpb24uY29t\n"
         + "gg95b3V0dWJla2lkcy5jb22CBXl0LmJlMCEGA1UdIAQaMBgwCAYGZ4EMAQICMAwG\n"
         + "CisGAQQB1nkCBQMwLwYDVR0fBCgwJjAkoCKgIIYeaHR0cDovL2NybC5wa2kuZ29v\n"
         + "Zy9HVFMxTzEuY3JsMIIBBQYKKwYBBAHWeQIEAgSB9gSB8wDxAHcAsh4FzIuizYog\n"
         + "Todm+Su5iiUgZ2va+nDnsklTLe+LkF4AAAFtt7HklQAABAMASDBGAiEAqQWtUhby\n"
         + "6kN7bmQ6+HsTWHnsJ6JfetP6BPXd21tzIY8CIQCpj3/wBTW5ak1bJh2yyBaEiYhL\n"
         + "X2U1QK/l6i1l3AbRhAB2AF6nc/nfVsDntTZIfdBJ4DJ6kZoMhKESEoQYdZaBcUVY\n"
         + "AAABbbex5KsAAAQDAEcwRQIhAK5DgdFa7XEEqngyBJzkPL11moosB06YVdEG/e2Z\n"
         + "4t+mAiBdH5bKDIqINpR32vBt8Nqp2L7f8e0jZLsQF/Pj3AP/5zANBgkqhkiG9w0B\n"
         + "AQsFAAOCAQEAAz/Zkc3geb2WF2T6csWwtFel8aWSXecEWG/xvO0HDlpCPCDUlauI\n"
         + "8LByL/gimC6Uwc4DJ8hZnr+sSELVo2dZhKhddF5n03VeJNIlOteW4+cFS5Yr2jxG\n"
         + "vLUtp997vv+rI5p73mWW06GaEJlloHA6M7rfpt6emE6rpX6KESN7mghWUgToyoVw\n"
         + "hRpGqCyTXvpFCqq9aOkFgPGJBL47NBHq2D7CbYMrooqsNiqZ1CtEWiAMjd2T9Uqz\n"
         + "DEXc6vVfSEpvdxjKQTqjxnc6grQsBWrVgHU/6+1NBhC5WBqO/INFln2gXuo1CMhr\n"
         + "Y37udPEQv3QqV2G0uJNcTjYyj1l45W8COA==\n"
         + "-----END CERTIFICATE-----";

    @Test
    public void shouldTrust() {
        assertTrue(Connect.isServerTrusted("https://google.com", GOOGLE_CERT));
    }

    @Test
    public void shouldNotTrust() {
        assertFalse(Connect.isServerTrusted("https://yahoo.com", GOOGLE_CERT));
    }

}
