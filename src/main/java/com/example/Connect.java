package com.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Connect {

    private static SSLContext createSslContext(String certificateString)
         throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {

        ByteArrayInputStream derInputStream = new ByteArrayInputStream(certificateString.getBytes());

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
        String alias = cert.getSubjectX500Principal().getName();

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry(alias, cert);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(trustStore, null);
        KeyManager[] keyManagers = kmf.getKeyManagers();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    }


    public static boolean isServerTrusted(String url, String certificateString) {

        try {
            URL urlToConnect = new URL(url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlToConnect.openConnection();
            httpsURLConnection.setSSLSocketFactory(createSslContext(certificateString).getSocketFactory());
            httpsURLConnection.setRequestMethod("GET");

            httpsURLConnection.setDoOutput(true);
            OutputStreamWriter o = new OutputStreamWriter(httpsURLConnection.getOutputStream());
            o.flush();
        } catch (sun.security.validator.ValidatorException | SSLHandshakeException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
