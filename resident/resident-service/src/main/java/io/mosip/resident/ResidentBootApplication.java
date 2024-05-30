package io.mosip.resident;

import io.mosip.kernel.pdfgenerator.itext.impl.PDFGeneratorImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

import io.mosip.idrepository.core.util.TokenIDGenerator;
import io.mosip.kernel.authcodeflowproxy.api.validator.ValidateTokenUtil;
import io.mosip.kernel.cbeffutil.impl.CbeffImpl;
import io.mosip.preregistration.application.service.TransliterationService;
import io.mosip.preregistration.application.service.util.TransliterationServiceUtil;

@SpringBootApplication(scanBasePackages = { 
		"io.mosip.resident.*", 
		"io.mosip.kernel.core.*",
		"io.mosip.kernel.crypto.jce.*", 
		"io.mosip.commons.packet.*", 
		"io.mosip.kernel.keygenerator.bouncycastle.*",
		"${mosip.auth.adapter.impl.basepackage}", 
		"io.mosip.kernel.virusscanner.*", 
		"io.mosip.commons.khazana.*",
		"io.mosip.idrepository.core.util.*"})
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@Import({TokenIDGenerator.class, ValidateTokenUtil.class, CbeffImpl.class, TransliterationService.class, TransliterationServiceUtil.class
, PDFGeneratorImpl.class})
public class ResidentBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResidentBootApplication.class, args);
	}

}
