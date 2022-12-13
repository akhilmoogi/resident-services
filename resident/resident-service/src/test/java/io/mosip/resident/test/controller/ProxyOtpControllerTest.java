package io.mosip.resident.test.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.mosip.kernel.core.authmanager.model.AuthNResponse;
import io.mosip.kernel.core.crypto.spi.CryptoCoreSpec;
import io.mosip.preregistration.application.constant.PreRegLoginConstant;
import io.mosip.resident.controller.ProxyOtpController;
import io.mosip.resident.dto.MainRequestDTO;
import io.mosip.resident.dto.MainResponseDTO;
import io.mosip.resident.dto.OtpRequestDTOV2;
import io.mosip.resident.helper.ObjectStoreHelper;
import io.mosip.resident.service.ProxyOtpService;
import io.mosip.resident.service.ResidentVidService;
import io.mosip.resident.service.impl.IdentityServiceImpl;
import io.mosip.resident.test.ResidentTestBootApplication;
import io.mosip.resident.util.AuditUtil;
import io.mosip.resident.util.Utilitiy;
import io.mosip.resident.validator.RequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Kamesh Shekhar Prasad
 * This class is used to test proxy otp controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResidentTestBootApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class ProxyOtpControllerTest {
	
    @MockBean
    private RequestValidator validator;

    @Mock
    private AuditUtil audit;
	
	@MockBean
	private ObjectStoreHelper objectStore;


    @MockBean
    @Qualifier("selfTokenRestTemplate")
    private RestTemplate residentRestTemplate;

    @InjectMocks
    ProxyOtpController proxyOtpController;

    @MockBean
    ProxyOtpService proxyOtpService;

    @MockBean
    IdentityServiceImpl identityService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResidentVidService vidService;

    @MockBean
    private CryptoCoreSpec<byte[], byte[], SecretKey, PublicKey, PrivateKey, String> encryptor;

    @MockBean
    private AuditUtil auditUtil;

    @Mock
    private Utilitiy utilitiy;

    @Mock
    private Environment environment;

    Gson gson = new GsonBuilder().serializeNulls().create();

    private MainRequestDTO<OtpRequestDTOV2> userOtpRequest;

    String reqJson;

    byte[] pdfbytes;

    private ResponseEntity<MainResponseDTO<AuthNResponse>> responseEntity;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(proxyOtpController).build();
        userOtpRequest = new MainRequestDTO<>();
        OtpRequestDTOV2 otpRequestDTOV2 = new OtpRequestDTOV2();
        otpRequestDTOV2.setUserId("8809909090");
        otpRequestDTOV2.setTransactionID("1234343434");
        userOtpRequest.setRequest(otpRequestDTOV2);
        userOtpRequest.setId("mosip.resident.contact.details.send.otp.id");
        reqJson = gson.toJson(userOtpRequest);
        AuthNResponse authNResponse = new AuthNResponse(PreRegLoginConstant.EMAIL_SUCCESS, PreRegLoginConstant.SUCCESS);
        MainResponseDTO<AuthNResponse> response = new MainResponseDTO<>();
        response.setResponse(authNResponse);
        responseEntity = new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    public void testSendOtp() throws Exception {
        Mockito.when(proxyOtpService.sendOtp(Mockito.any())).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.post("/contact-details/send-otp").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(reqJson.getBytes())).andExpect(status().isOk());
    }

    @Test
    public void testValidateOtp() throws Exception {
        Mockito.when(proxyOtpService.validateWithUserIdOtp(Mockito.any())).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.post("/contact-details/update-data").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(reqJson.getBytes())).andExpect(status().isOk());
    }

}