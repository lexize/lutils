# LJsonSerializer
## Functions
**serialize(Object arg0) -> String**

**deserialize(String arg0) -> Object**

**newProvider() -> [LJsonProvider](./LJsonProvider.md)**\
Creates new JSON provider with UTF8 encoding

**newProvider(String encoding) -> [LJsonProvider](./LJsonProvider.md)**\
Creates new JSON provider with specified encoding

**newProvider([LStringProvider](./LStringProvider.md) provider) -> [LJsonProvider](./LJsonProvider.md)**\
Creates new JSON provider with encoding taken from specified string provider

**newReader() -> [LJsonProvider](./LJsonProvider.md)**\
Creates new JSON reader with UTF8 encoding

**newReader(String encoding) -> [LJsonProvider](./LJsonProvider.md)**\
Creates new JSON reader with specified encoding

**newReader([LStringReader](./LStringReader.md) reader) -> [LJsonProvider](./LJsonProvider.md)**\
Creates new JSON reader with encoding taken from specified string reader