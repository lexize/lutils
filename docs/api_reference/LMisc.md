# LMisc
Submodule functions that cant be categorized
## Fields
**LuaTable encodings**
## Functions
**stringToBytes(String string, String charset) -> byte[]**\
Gets bytes of specified string with specified charset. If charset not specified UTF_8 will be used

**bytesToString(byte[] bytes, String charset) -> String**\
Gets bytes of specified string with specified charset. If charset not specified UTF_8 will be used

**base64ToBytes(String base64) -> byte[]**\
Decodes provided Base64 string and returns bytes of it

**bytesToBase64(byte[] bytes) -> String**\
Encodes bytes to Base64 string