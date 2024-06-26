package io.mosip.resident.constant;

import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;

/**
 * Enum to store the TransactionStage
 * 
 * @author Kamesh Shekhar Prasad
 */
public enum PacketStatus {
	SUCCESS("SUCCESS", "resident.success.packet-status-code.list"),
	IN_PROGRESS("IN-PROGRESS", "resident.in-progress.packet-status-code.list"),
	FAILURE("FAILURE", "resident.failure.packet-status-code.list");

	private String name;
	private String statusCodePropertyName;

	PacketStatus(String name, String statusCodePropertyName) {
		this.name = name;
		this.statusCodePropertyName = statusCodePropertyName;
	}

	public String getName() {
		return name;
	}

	public static Optional<String> getStatusCode(String statusCode, Environment env) {
		for (PacketStatus packetStatus : values()) {
			if (getStatusCodeList(packetStatus, env).contains(statusCode)) {
				return Optional.of(packetStatus.name);
			}
		}
		return Optional.empty();
	}

	public static List<String> getStatusCodeList(PacketStatus packetStatus, Environment env) {
		List<String> statusCode = List.of();
		if (packetStatus.statusCodePropertyName != null) {
			String propertyName = env.getProperty(packetStatus.statusCodePropertyName);
			if (propertyName != null) {
				statusCode = List.of(propertyName.split(","));
			}
		}
		return statusCode;
	}

}
