# LJavaInputStream
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