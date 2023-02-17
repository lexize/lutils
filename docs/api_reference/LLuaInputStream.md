# LLuaInputStream
## Fields
**LuaFunction onRead**\
Function called when read() method is called. Should return int\
**LuaFunction onSkip**\
Function called when skip() method is called. Should return long\
**LuaFunction availableCount**\
Function that should return amount of bytes available to read\
**LuaFunction onClose**\
Function called when close() method is called.\
**LuaFunction onMark**\
Function called when mark() method is called.\
**LuaFunction onReset**\
Function called when reset() method is called.\
**LuaFunction isMarkSupported**\
Function that should return true if mark supported for this stream, false otherwise
## Functions
**read() -> int**\
Reads byte from stream

**close()**\
Closes this stream

**mark(int readlimit)**\
Marks current position in this input stream. You can get back to it with reset()

**skip(long arg0) -> long**\
Skips specified amount of bytes in stream, and returns how much bytes was actually skipped

**available() -> int**\
Returns amount of bytes available

**markSupported() -> boolean**\
Is mark supported for this stream

**reset()**\
Resets stream position to mark

**transferTo(OutputStream arg0) -> long**\
Transfers remaining data from this stream to specified output stream