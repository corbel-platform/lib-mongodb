package io.corbel.lib.mongo;

/**
 * @author Alexander De Leon
 * 
 */
public class SafeKeys {

	static final String ESCAPED_ID = "__id";
	static final String MONGO_ID = "_id";
	static final String ID = "id";

	public static String getSafeKey(String key) {
		switch (key) {
			case ID:
				return MONGO_ID;
			case MONGO_ID:
				return ESCAPED_ID;
			default:
				return key;
		}
	}

    public static String getOriginalKey(String key) {
        switch (key) {
            case ESCAPED_ID:
                return MONGO_ID;
            case MONGO_ID:
                return ID;
            default:
                return key;
        }
    }

}
