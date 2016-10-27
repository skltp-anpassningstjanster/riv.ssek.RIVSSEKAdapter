/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skl.skltpservices.ssekadapter.mapper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Utility class to create and map common EHR types.
 * 
 * @author torbjorncla
 */
public final class SSEKUtil {

    public static final String CAREUNITHSAIDS    = "careUnitHsaIds";
    public static final String INFORMATIONSÄGARE = "iag";
    


    private static ThreadLocal<SimpleDateFormat> yyyyMMddFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    public static String formatTimestamp(Date timestamp) {
        return yyyyMMddFormatter.get().format(timestamp);
    }
    //
    
    public static Date parseTimePeriod(String dateString) throws ParseException {
        return yyyyMMddFormatter.get().parse(dateString);
    }

 
    public static <T> T firstItem(final List<T> list) {
        return (list.size() == 0) ? null : list.get(0);
    }
 
    /**
     * Removes a string prefix on match.
     *
     * @param value
     *            the string.
     * @param prefix
     *            the prefix to remove.
     * @return the string without prefix, i.e. unchanged if the prefix doesn't match.
     */
    public static String removePrefix(final String value, final String prefix) {
        return (value == null) ? null : value.replaceFirst(prefix, "");
    }
    
    
    public static void setDocumentTitle() {
        
    }
    
 
        	

 

    /**
     * Returns a {@link Date} date and time representation.
     *
     * @param cal
     *            the actual date and time.
     * @return the {@link Date} representation.
     */
    public static Date toDate(XMLGregorianCalendar cal) {
        if (cal != null) {
            final Calendar c = Calendar.getInstance();

            c.set(Calendar.DATE, cal.getDay());
            c.set(Calendar.MONTH, cal.getMonth() - 1);
            c.set(Calendar.YEAR, cal.getYear());
            c.set(Calendar.DAY_OF_MONTH, cal.getDay());
            c.set(Calendar.HOUR_OF_DAY, cal.getHour());
            c.set(Calendar.MINUTE, cal.getMinute());
            c.set(Calendar.SECOND, cal.getSecond());
            c.set(Calendar.MILLISECOND, cal.getMillisecond());

            return c.getTime();
        }
        return null;
    }
    
    
    /**
     * Timestamp in format yyyyMMddHHmmss - sometimes needs to be padded with zeros. 
     */
    public static String padTimestampIfNecessary(String timestamp) {
        if (timestamp == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(timestamp);
        while (result.length() < "yyyyMMddHHmmss".length()) {
            result.append("0");
        }
        return result.toString();
    }


    // --- --------------------------------------------------------------------
    

    // Generic baseline of data types to be able to convert between schemas (java packages).
    //
    public static class Result {
        private ResultCode resultCode;
        private ErrorCode errorCode;
        private String logId;
        private String subCode;
        private String message;

        public Result() {
            super();
        }

        public Result(ResultCode resultCode, ErrorCode errorCode, String logId, String subCode, String message) {
            super();
            this.resultCode = resultCode;
            this.errorCode = errorCode;
            this.logId = logId;
            this.subCode = subCode;
            this.message = message;
        }

        public ResultCode getResultCode() {
            return resultCode;
        }

        public void setResultCode(ResultCode resultCode) {
            this.resultCode = resultCode;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ErrorCode errorCode) {
            this.errorCode = errorCode;
        }

        public String getLogId() {
            return logId;
        }

        public void setLogId(String logId) {
            this.logId = logId;
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    //
    public static enum ResultCode {
        OK, ERROR, INFO;
    }

    //
    public static enum ErrorCode {
        INVALID_REQUEST, TRANSFORMATION_ERROR, APPLICATION_ERROR, TECHNICAL_ERROR;
    }

    public static class TimePeriod {
        private String start;
        private String end;

        public TimePeriod() {
            super();
        }

        public TimePeriod(String start, String end) {
            super();
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

    }

    public static class Request {
        //
        private PersonId patientId;

        public PersonId getPatientId() {
            return patientId;
        }

        public void setPatientId(PersonId patientId) {
            this.patientId = patientId;
        }

        //
        private DatePeriod timePeriod;

        public DatePeriod getTimePeriod() {
            return timePeriod;
        }

        public void setTimePeriod(DatePeriod timePeriod) {
            this.timePeriod = timePeriod;
        }
        
        // ImagingOutcome, MedicationHistory, ReferralOutcome
        
        public DatePeriod getDatePeriod() {
            return timePeriod;
        }
        
        public void setDatePeriod(DatePeriod datePeriod) {
            timePeriod = datePeriod;
        }

    }

    //
    public static class DatePeriod {
        private String start;
        private String end;

        public DatePeriod() {
            super();
        }

        public DatePeriod(String start, String end) {
            super();
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

    }

    //
    public static class PersonId {
        private String id;
        private String type;

        public PersonId() {
            super();
        }

        public PersonId(String id, String type) {
            super();
            this.id = id;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class IIType {
        private String extension;
        private String root;

        public IIType() {
            super();
        }

        public IIType(String extension, String root) {
            super();
            this.extension = extension;
            this.root = root;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }
    }

    //
    public static class CV {
        private String code;
        private String codeSystem;
        private String codeSystemName;
        private String codeSystemVersion;
        private String displayName;
        private String originalText;

        public CV() {
            super();
        }

        public CV(String code, String codeSystem, String codeSystemName, String codeSystemVersion, String displayName, String originalText) {
            super();
            this.code = code;
            this.codeSystem = codeSystem;
            this.codeSystemName = codeSystemName;
            this.codeSystemVersion = codeSystemVersion;
            this.displayName = displayName;
            this.originalText = originalText;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCodeSystem() {
            return codeSystem;
        }

        public void setCodeSystem(String codeSystem) {
            this.codeSystem = codeSystem;
        }

        public String getCodeSystemName() {
            return codeSystemName;
        }

        public void setCodeSystemName(String codeSystemName) {
            this.codeSystemName = codeSystemName;
        }

        public String getCodeSystemVersion() {
            return codeSystemVersion;
        }

        public void setCodeSystemVersion(String codeSystemVersion) {
            this.codeSystemVersion = codeSystemVersion;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getOriginalText() {
            return originalText;
        }

        public void setOriginalText(String originalText) {
            this.originalText = originalText;
        }
    }

    //
    public static class HealthcareProfessional {
        private String authorTime;
        private String healthcareProfessionalHSAId;
        private String healthcareProfessionalName;
        private CV healthcareProfessionalRoleCode;
        private OrgUnit healthcareProfessionalOrgUnit;
        private String healthcareProfessionalCareUnitHSAId;
        private String healthcareProfessionalCareGiverHSAId;

        public HealthcareProfessional() {
            super();
        }

        public HealthcareProfessional(String authorTime, String healthcareProfessionalHSAId, String healthcareProfessionalName,
                CV healthcareProfessionalRoleCode, OrgUnit healthcareProfessionalOrgUnit, String healthcareProfessionalCareUnitHSAId,
                String healthcareProfessionalCareGiverHSAId) {
            super();
            this.authorTime = authorTime;
            this.healthcareProfessionalHSAId = healthcareProfessionalHSAId;
            this.healthcareProfessionalName = healthcareProfessionalName;
            this.healthcareProfessionalRoleCode = healthcareProfessionalRoleCode;
            this.healthcareProfessionalOrgUnit = healthcareProfessionalOrgUnit;
            this.healthcareProfessionalCareUnitHSAId = healthcareProfessionalCareUnitHSAId;
            this.healthcareProfessionalCareGiverHSAId = healthcareProfessionalCareGiverHSAId;
        }

        public String getAuthorTime() {
            return authorTime;
        }

        public void setAuthorTime(String authorTime) {
            this.authorTime = authorTime;
        }

        public String getHealthcareProfessionalHSAId() {
            return healthcareProfessionalHSAId;
        }

        public void setHealthcareProfessionalHSAId(String healthcareProfessionalHSAId) {
            this.healthcareProfessionalHSAId = healthcareProfessionalHSAId;
        }

        public String getHealthcareProfessionalName() {
            return healthcareProfessionalName;
        }

        public void setHealthcareProfessionalName(String healthcareProfessionalName) {
            this.healthcareProfessionalName = healthcareProfessionalName;
        }

        public CV getHealthcareProfessionalRoleCode() {
            return healthcareProfessionalRoleCode;
        }

        public void setHealthcareProfessionalRoleCode(CV healthcareProfessionalRoleCode) {
            this.healthcareProfessionalRoleCode = healthcareProfessionalRoleCode;
        }

        public OrgUnit getHealthcareProfessionalOrgUnit() {
            return healthcareProfessionalOrgUnit;
        }

        public void setHealthcareProfessionalOrgUnit(OrgUnit healthcareProfessionalOrgUnit) {
            this.healthcareProfessionalOrgUnit = healthcareProfessionalOrgUnit;
        }

        public String getHealthcareProfessionalCareUnitHSAId() {
            return healthcareProfessionalCareUnitHSAId;
        }

        public void setHealthcareProfessionalCareUnitHSAId(String healthcareProfessionalCareUnitHSAId) {
            this.healthcareProfessionalCareUnitHSAId = healthcareProfessionalCareUnitHSAId;
        }

        public String getHealthcareProfessionalCareGiverHSAId() {
            return healthcareProfessionalCareGiverHSAId;
        }

        public void setHealthcareProfessionalCareGiverHSAId(String healthcareProfessionalCareGiverHSAId) {
            this.healthcareProfessionalCareGiverHSAId = healthcareProfessionalCareGiverHSAId;
        }

    }

    //
    public static class OrgUnit {
        private String orgUnitHSAId;
        private String orgUnitName;
        private String orgUnitTelecom;
        private String orgUnitEmail;
        private String orgUnitAddress;
        private String orgUnitLocation;

        public OrgUnit() {
            super();
        }

        public OrgUnit(String orgUnitHSAId, String orgUnitName, String orgUnitTelecom, String orgUnitEmail, String orgUnitAddress,
                String orgUnitLocation) {
            super();
            this.orgUnitHSAId = orgUnitHSAId;
            this.orgUnitName = orgUnitName;
            this.orgUnitTelecom = orgUnitTelecom;
            this.orgUnitEmail = orgUnitEmail;
            this.orgUnitAddress = orgUnitAddress;
            this.orgUnitLocation = orgUnitLocation;
        }

        public String getOrgUnitHSAId() {
            return orgUnitHSAId;
        }

        public void setOrgUnitHSAId(String orgUnitHSAId) {
            this.orgUnitHSAId = orgUnitHSAId;
        }

        public String getOrgUnitName() {
            return orgUnitName;
        }

        public void setOrgUnitName(String orgUnitName) {
            this.orgUnitName = orgUnitName;
        }

        public String getOrgUnitTelecom() {
            return orgUnitTelecom;
        }

        public void setOrgUnitTelecom(String orgUnitTelecom) {
            this.orgUnitTelecom = orgUnitTelecom;
        }

        public String getOrgUnitEmail() {
            return orgUnitEmail;
        }

        public void setOrgUnitEmail(String orgUnitEmail) {
            this.orgUnitEmail = orgUnitEmail;
        }

        public String getOrgUnitAddress() {
            return orgUnitAddress;
        }

        public void setOrgUnitAddress(String orgUnitAddress) {
            this.orgUnitAddress = orgUnitAddress;
        }

        public String getOrgUnitLocation() {
            return orgUnitLocation;
        }

        public void setOrgUnitLocation(String orgUnitLocation) {
            this.orgUnitLocation = orgUnitLocation;
        }

    }

    //
    public static class PatientSummaryHeader {
        private String documentId;
        private String sourceSystemHSAId;
        private String documentTitle;
        private String documentTime;
        private PersonId patientId;
        private HealthcareProfessional accountableHealthcareProfessional;
        private LegalAuthenticator legalAuthenticator;
        private boolean approvedForPatient = false;
        private String careContactId;

        public String getDocumentId() {
            return documentId;
        }

        public String getSourceSystemHSAId() {
            return sourceSystemHSAId;
        }

        public String getDocumentTitle() {
            return documentTitle;
        }

        public String getDocumentTime() {
            return documentTime;
        }

        public PersonId getPatientId() {
            return patientId;
        }

        public HealthcareProfessional getAccountableHealthcareProfessional() {
            return accountableHealthcareProfessional;
        }

        public LegalAuthenticator getLegalAuthenticator() {
            return legalAuthenticator;
        }

        public boolean isApprovedForPatient() {
            return approvedForPatient;
        }

        public String getCareContactId() {
            return careContactId;
        }
    }

    //
    public static class LegalAuthenticator {
        private String signatureTime;
        private String legalAuthenticatorHSAId;
        private String legalAuthenticatorName;
        private CV legalAuthenticatorRoleCode;

        public LegalAuthenticator() {
            super();
        }

        public LegalAuthenticator(String signatureTime, String legalAuthenticatorHSAId, String legalAuthenticatorName, CV legalAuthenticatorRoleCode) {
            super();
            this.signatureTime = signatureTime;
            this.legalAuthenticatorHSAId = legalAuthenticatorHSAId;
            this.legalAuthenticatorName = legalAuthenticatorName;
            this.legalAuthenticatorRoleCode = legalAuthenticatorRoleCode;
        }

        public String getSignatureTime() {
            return signatureTime;
        }

        public void setSignatureTime(String signatureTime) {
            this.signatureTime = signatureTime;
        }

        public String getLegalAuthenticatorHSAId() {
            return legalAuthenticatorHSAId;
        }

        public void setLegalAuthenticatorHSAId(String legalAuthenticatorHSAId) {
            this.legalAuthenticatorHSAId = legalAuthenticatorHSAId;
        }

        public String getLegalAuthenticatorName() {
            return legalAuthenticatorName;
        }

        public void setLegalAuthenticatorName(String legalAuthenticatorName) {
            this.legalAuthenticatorName = legalAuthenticatorName;
        }

        public CV getLegalAuthenticatorRoleCode() {
            return legalAuthenticatorRoleCode;
        }

        public void setLegalAuthenticatorRoleCode(CV legalAuthenticatorRoleCode) {
            this.legalAuthenticatorRoleCode = legalAuthenticatorRoleCode;
        }
    }
}
