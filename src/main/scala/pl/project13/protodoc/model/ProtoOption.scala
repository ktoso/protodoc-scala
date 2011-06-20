package pl.project13.protodoc.model

/**
 * Individual declarations in a .proto file can be annotated with a number of options.
 * Options do not change the overall meaning of a declaration, but may affect the way it
 * is handled in a particular context. The complete list of available options is defined in
 * google/protobuf/descriptor.proto.
 *
 * Some options are file-level options, meaning they should be written at the top-level scope,
 * not inside any message, enum, or service definition. Some options are message-level options,
 * meaning they should be written inside message definitions. Options can also be written on fields,
 * enum types, enum values, service types, and service methods; however, no useful options currently
 * exist for any of these.
 *
 * <pre>
 *   <code>
 *   option java_package = "com.example.foo";
 *   option java_outer_classname = "Ponycopter";
 *   option optimize_for = CODE_SIZE; // LITE_RUNTIME or SPEED (default)
 *   </code>
 * </pre>
 */
case class ProtoOption(optionName: String,
                       value: String)

case class ProtoCustomOptionDefinition(customOptionName: String,
                                       value: String)