# LJson
## Functions
**newSerializer(LuaTable settings) -> [LJsonSerializer](./LJsonSerializer.md)**\
Returns new serializer with specified settings.\
If settings will be nil - serializer will be created with default settings.\
Available settings:\
**prettyPrinting** - boolean. Default - false\
**htmlEscaping** - boolean. Default - true\
**serializeNulls** - boolean. Default - false