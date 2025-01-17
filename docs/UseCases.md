# Use Cases

## start mongod                                       

```java
Transitions transitions = Mongod.instance().transitions(Version.Main.PRODUCTION);

try (TransitionWalker.ReachedState<RunningMongodProcess> running = transitions.walker()
  .initState(StateID.of(RunningMongodProcess.class))) {

  try (MongoClient mongo = new MongoClient(serverAddress(running.current().getServerAddress()))) {
...

  }
}

```

![start mongod](UseCase-Mongod.svg)
               
## json import with mongoimport into mongod

```java
MongoImportArguments arguments = MongoImportArguments.builder()
  .databaseName("importDatabase")
  .collectionName("importCollection")
  .importFile(Resources.getResource("sample.json").getFile())
  .isJsonArray(true)
  .upsertDocuments(true)
  .build();

Version.Main version = Version.Main.PRODUCTION;

try (TransitionWalker.ReachedState<RunningMongodProcess> mongoD = Mongod.instance().transitions(version)
  .walker()
  .initState(StateID.of(RunningMongodProcess.class))) {

  Transitions mongoImportTransitions = MongoImport.instance()
    .transitions(version)
    .replace(Start.to(MongoImportArguments.class).initializedWith(arguments))
    .addAll(Start.to(ServerAddress.class).initializedWith(mongoD.current().getServerAddress()));

  try (TransitionWalker.ReachedState<ExecutedMongoImportProcess> executed = mongoImportTransitions.walker()
    .initState(StateID.of(ExecutedMongoImportProcess.class))) {
...

  }

  try (MongoClient mongo = new MongoClient(serverAddress(mongoD.current().getServerAddress()))) {
    MongoDatabase db = mongo.getDatabase("importDatabase");
    MongoCollection<Document> col = db.getCollection("importCollection");

    ArrayList<String> names = col.find()
      .map(doc -> doc.getString("name"))
      .into(new ArrayList<>());

    assertThat(names).containsExactlyInAnyOrder("Cassandra", "HBase", "MongoDB");
  }
}
```

![start mongod](UseCase-MongoImport.svg)

## json import with mongoimport into mongod - compact version

```java
ImmutableMongoImportArguments arguments = MongoImportArguments.builder()
  .databaseName("importDatabase")
  .collectionName("importCollection")
  .importFile(Resources.getResource("sample.json").getFile())
  .isJsonArray(true)
  .upsertDocuments(true)
  .build();

Version.Main version = Version.Main.PRODUCTION;

Transitions mongoImportTransitions = MongoImport.instance().transitions(version)
  .replace(Start.to(MongoImportArguments.class).initializedWith(arguments))
  .addAll(Derive.given(RunningMongodProcess.class).state(ServerAddress.class)
    .deriveBy(Try.function(RunningMongodProcess::getServerAddress).mapToUncheckedException(RuntimeException::new)))
  .addAll(Mongod.instance().transitions(version).walker()
    .asTransitionTo(TransitionMapping.builder("mongod", StateID.of(RunningMongodProcess.class))
      .build()));

try (TransitionWalker.ReachedState<RunningMongodProcess> mongoD = mongoImportTransitions.walker()
  .initState(StateID.of(RunningMongodProcess.class))) {

  try (TransitionWalker.ReachedState<ExecutedMongoImportProcess> running = mongoD.initState(StateID.of(ExecutedMongoImportProcess.class))) {
...

  }

  try (MongoClient mongo = new MongoClient(serverAddress(mongoD.current().getServerAddress()))) {
    MongoDatabase db = mongo.getDatabase("importDatabase");
    MongoCollection<Document> col = db.getCollection("importCollection");

    ArrayList<String> names = col.find()
      .map(doc -> doc.getString("name"))
      .into(new ArrayList<>());

    assertThat(names).containsExactlyInAnyOrder("Cassandra", "HBase", "MongoDB");
  }
}
```

![start mongod](UseCase-Mongod-MongoImport.svg)
