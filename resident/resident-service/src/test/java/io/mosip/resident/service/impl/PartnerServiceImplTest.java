package io.mosip.resident.service.impl;

import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.websub.spi.PublisherClient;
import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import io.mosip.kernel.websub.api.model.SubscriptionChangeResponse;
import io.mosip.kernel.websub.api.model.UnsubscriptionRequest;
import io.mosip.resident.exception.ApisResourceAccessException;
import io.mosip.resident.exception.ResidentServiceCheckedException;
import io.mosip.resident.util.ResidentServiceRestClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@RefreshScope
@ContextConfiguration
public class PartnerServiceImplTest {

	@Mock
	private ResidentServiceRestClient residentServiceRestClient;

	@Mock
	Environment env;

	@InjectMocks
	private PartnerServiceImpl partnerService;

	@Mock
	private IdentityServiceImpl identityServiceImpl;

	@Mock
	private PublisherClient<String, Object, HttpHeaders> publisher;

	@Mock
	SubscriptionClient<SubscriptionChangeRequest, UnsubscriptionRequest, SubscriptionChangeResponse> subscribe;

	@Mock
	private ProxyPartnerManagementServiceImpl proxyPartnerManagementService;

	private ResponseWrapper responseWrapper;

	@Before
	public void setup() {
		responseWrapper = new ResponseWrapper<Map<String, Object>>();
		responseWrapper.setVersion("v1");
		responseWrapper.setId("1");
		Map<String, Object> partners = new HashMap<>();
		ArrayList<Object> partnerList = new ArrayList<>();
		Map<String, Object> individualPartner = new HashMap<>();
		individualPartner.put("partnerID", "1");
		partnerList.add(individualPartner);
		partners.put("partners", partnerList);
		responseWrapper.setResponse(partners);
	}

	@Test
	public void testPartnerService()
			throws ResidentServiceCheckedException {
		when(proxyPartnerManagementService.getPartnersByPartnerType(any())).thenReturn(responseWrapper);
		ArrayList<String> partnerIds = partnerService.getPartnerDetails("Online_Verification_Partner");
		assertEquals(1, partnerIds.size());
	}

	@Test(expected = ResidentServiceCheckedException.class)
	public void testPartnerServiceCatchBlock()
			throws ResidentServiceCheckedException {
		when(proxyPartnerManagementService.getPartnersByPartnerType(any())).thenThrow(new ResidentServiceCheckedException());
		partnerService.getPartnerDetails("Online_Verification_Partner");
	}

	@Test
	public void testPartnerServiceResponseNull()
			throws ResidentServiceCheckedException {
		when(proxyPartnerManagementService.getPartnersByPartnerType(any())).thenReturn(null);
		ArrayList<String> partnerIds = partnerService.getPartnerDetails("Online_Verification_Partner");
		assertEquals(0, partnerIds.size());
	}

	@Test
	public void testPartnerIdNull()
			throws ResidentServiceCheckedException, ApisResourceAccessException, URISyntaxException {
		ArrayList<String> partnerIds = partnerService.getPartnerDetails(null);
		assertEquals(0, partnerIds.size());
	}

	@Test
	public void testPartnerServiceUrlNull()
			throws ResidentServiceCheckedException, ApisResourceAccessException, URISyntaxException {
		ArrayList<String> partnerIds = partnerService.getPartnerDetails("Online_Verification_Partner");
		assertEquals(0, partnerIds.size());
	}
}