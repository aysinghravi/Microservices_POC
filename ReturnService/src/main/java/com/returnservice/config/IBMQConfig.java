package com.returnservice.config;


import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.returnservice.exception.JMSErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

@Slf4j
@Configuration
@PropertySources({@PropertySource("classpath:application.properties")})
public class IBMQConfig {

    @Value("${wmq.host.name}")
    private String wmqHostName;
    @Value("${wmq.port}")
    private int wmqPort;
    @Value("${wmq.channel}")
    private String wmqChannel;
    @Value("${wmq.connection.mode}")
    private int wmqConnectionMode;
    @Value("${wmq.queue.manager}")
    private String wmqQueueManager;
    @Value("${wmq.ssl.cipher.suite}")
    private String wmqSslCipherSuite;
    @Value("${wmq.user.auth.mqcp}")
    private boolean userAuthenticationMqcsp;
    @Value("${wmq.ssl.enable}")
    private boolean isSSL;

    @Value("${ibm.cipher.mappings}")
    private String ibmqCipherMappingsValue;
    @Value("${ibm.javax.ssl.keystore}")
    private String sslKeystorePath;
    @Value("${ibm.javax.ssl.keystore.pwd}")
    private String sslKeystorePwd;
    @Value("${ibm.javax.ssl.truststore}")
    private String sslTruststorePath;
    @Value("${ibm.javax.ssl.truststore.pwd}")
    private String sslTruststorePwd;
    @Value("${ibm.javax.ssl.context}")
    private String sslContext;

    public static final String IBMQ_CIPHER_MAPPINGS = "com.ibm.mq.cfg.useIBMCipherMappings";
    @Bean
    public JmsListenerContainerFactory<?> mqQueueConnectionFactory(
            ConnectionFactory connectionFactory, @Autowired JMSErrorHandler errorHandler) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        System.setProperty(IBMQ_CIPHER_MAPPINGS, ibmqCipherMappingsValue);
        try {
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory connectionFactory = ff.createConnectionFactory();

            if (isSSL) {
                //keystore truststore changes starts here
                KeyStore ks = KeyStore.getInstance("JKS");
                try (InputStream keyStoreStream = Files.newInputStream(Paths.get(sslKeystorePath))) {
                    ks.load(keyStoreStream, sslKeystorePwd.toCharArray());
                }
                KeyStore trustStore = KeyStore.getInstance("JKS");
                try (InputStream trustStoreStream = Files.newInputStream(Paths.get(sslTruststorePath))) {
                    trustStore.load(trustStoreStream, sslTruststorePwd.toCharArray());
                }
                // Create a default trust and key manager
                TrustManagerFactory trustManagerFactory =
                        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                KeyManagerFactory keyManagerFactory =
                        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

                // Initialise the managers
                trustManagerFactory.init(trustStore);
                keyManagerFactory.init(ks, sslKeystorePwd.toCharArray());
                SSLContext contextInstance = SSLContext.getInstance(this.sslContext);

                // Initialise our SSL context from the key/trust managers
                contextInstance.init(keyManagerFactory.getKeyManagers(),
                        trustManagerFactory.getTrustManagers(), null);
                SSLSocketFactory sslSocketFactory = contextInstance.getSocketFactory();

                ((MQConnectionFactory) connectionFactory).setSSLSocketFactory(sslSocketFactory);
                //keystore truststore changes ends here
                connectionFactory.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, wmqSslCipherSuite);
            }

            connectionFactory.setStringProperty(WMQConstants.WMQ_HOST_NAME, wmqHostName);
            connectionFactory.setIntProperty(WMQConstants.WMQ_PORT, wmqPort);
            connectionFactory.setStringProperty(WMQConstants.WMQ_CHANNEL, wmqChannel);
            connectionFactory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            connectionFactory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, wmqQueueManager);
            connectionFactory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, userAuthenticationMqcsp);
            return connectionFactory;
        } catch (JMSException | KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                 UnrecoverableKeyException | KeyManagementException e) {
            log.error("Got error : ", e);
        }
        return null;
    }
}
