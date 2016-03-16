package cl.buildersoft.bean.sso;

import java.sql.Timestamp;

import cl.buildersoft.framework.beans.BSBean;

public class SessionBean extends BSBean {
	private static final long serialVersionUID = -3842283210305227641L;
	@SuppressWarnings("unused")
	private String TABLE = "bsframework.tSession";

	private String sessionId = null;
	private Timestamp lastAccess = null;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Timestamp getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Timestamp lastAccess) {
		this.lastAccess = lastAccess;
	}

}
