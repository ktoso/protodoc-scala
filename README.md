ProtoDoc - It's like JavaDoc, but for Google Protocol Buffers
=============================================================
Have you ever worked with a BIG Protocol Buffers based application? It would be awesome if it had some kind 
of tool like JavaDoc, to always know what each field exactly means, even without having the proto file at hand right now.

*Oh, wait. That's excatly what ProtoDoc is!* ProtoDoc is a tool very much like JavaDoc, which scans your proto files and generates
an easily searchable and most informative website with all information about yout ProtoMessages.

**It's currently not production ready, and allows only for basic Messages to be parsed.** But it should most probably be finished and fully working quite soon.

Coding notes
------------

* ProtoDoc is build by **SBT** so if you want to help out hacking, first download **Scala 2.8.1** and **sbt** :-)

Output screenshots
------------------
ProtoDoc takes this:

```
/** Awesome documentation */
message AwesomeMessage {
 // ...
}
```

and generates this:<br/>

<img src="https://raw.github.com/ktoso/protodoc-scala/master/doc/protodoc_main.png" alt="protodoc sample"/>

<br/>
Hooray!
