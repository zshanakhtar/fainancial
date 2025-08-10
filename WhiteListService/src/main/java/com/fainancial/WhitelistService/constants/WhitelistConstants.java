package com.fainancial.WhitelistService.constants;

public class WhitelistConstants {
    
    public static final String UPLOAD_COMPLETED_QUEUE = "upload.completed";
    public static final String WHITELIST_COMPLETED_QUEUE = "whitelist.completed";
    
    
    public static final String FORM_16 = "Form 16";
    public static final String UNKNOWN_DOCUMENT = "Unknown";
    

    public static final long MAX_FILE_SIZE_BYTES = 50 * 1024 * 1024; // 50MB
    public static final String[] SUPPORTED_EXTENSIONS = {".pdf", ".doc", ".docx", ".txt"};
    
   
    public static final String[] FORM_16_FILENAME_PATTERNS = {
        "form16", "form_16", "form-16"
    };
    
    public static final String[] FORM_16_CONTENT_PATTERNS = {
        "Form No. 16", 
        "Certificate under section 203", 
        "Assessment Year", 
        "Employee's PAN",
        "FORM 16",
        "Income Tax Department"
    };
    
    public static final double FILENAME_CONFIDENCE_BOOST = 0.7;
    public static final double CONTENT_CONFIDENCE_BOOST = 0.3;
    public static final double MIN_CONFIDENCE_THRESHOLD = 0.5;
}