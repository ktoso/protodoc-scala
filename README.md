ProtoDoc - It's like JavaDoc, but for Google Protocol Buffers
=============================================================
Have you ever worked with a BIG Protocol Buffers based application? It would be awesome if it had some kind 
of tool like JavaDoc, to always know what each field exactly means, even without having the proto file at hand right now.

*Oh, wait. That's excatly what ProtoDoc is!* ProtoDoc is a tool very much like JavaDoc, which scans your proto files and generates
an easily searchable and most informative website with all information about yout ProtoMessages.

**It's currently not production ready, and allows only for basic Messages to be parsed.** But it should most probably be finished and fully working quite soon.

Live Demo
---------
There is a <a href="http://www.up.project13.pl/protodoc/index.html">live demo available here</a>.

Coding notes
------------

* ProtoDoc is build by **SBT** so if you want to help out hacking, first download **Scala 2.8.1** and **sbt** :-)

Output screenshots
------------------
ProtoDoc takes this:

```
package pl.project13;

/**
 * This is a simple Message which has some Inner Message defined.
 * Also, note that it has a default value on the name property.
 */
message MessageWithInner {
    /**
     * A number is just a simple property
     */
    required int32 number = 1;

    /**
     * This can be a name of your likeing, the default value is "lorem ipsum" etc
     */
    required string name = 2 [default = "loremipsum"];


    /**
     * Whoa, this is a comment on an inner message!
     * ProtoDoc is so cool!
     */
    message InnerMessage {

        /**
         * This is a comment on an inner messages field, cool~<br/>
         * <br/>
         * You may use it like this in your generated Java code:
         * <pre><code> setName("StringMyName");</code></pre>
         */
        optional string name = 3;
    }
}
```

and generates this:<br/>

<img src="https://raw.github.com/ktoso/protodoc-scala/master/doc/protodoc_main.png" alt="protodoc sample"/>

<br/>
Hooray!
