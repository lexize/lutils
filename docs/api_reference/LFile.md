# LFile
## Functions
**list(String path) -> String[]**\
Returns table with file paths in specified directory

**write(String path, Object data, [LProvider](./LProvider.md) provider)**\
Writes data in file by specified path with specified provider

**read(String arg0, [LReader](./LReader.md) arg1) -> Object**\
Reads file with specified reader

**delete(String arg0) -> boolean**\
Deletes file/folder by specified path

**exists(String arg0) -> boolean**\
Checks, is element by specified path exists

**isDirectory(String arg0) -> boolean**\
Checks, is element by specified path is directory

**mkdir(String arg0) -> boolean**\
Creates folder by specified path

**mkdirs(String arg0) -> boolean**\
Creates all folders in specified path if they're not exists

**isFile(String arg0) -> boolean**\
Checks, is element by specified path is file

**setFolderName(String arg0)**\
Sets data folder name

**getFolderName() -> String**\
Returns data folder name

**openInputStream(String arg0) -> [LInputStream](./LInputStream.md)**\
Opens input stream for file by specified path

**openOutputStream(String arg0) -> [LOutputStream](./LOutputStream.md)**\
Opens output stream for file by specified path