// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: H264.proto

package com.apron.mobilesdk.state;

public final class ProtoH264 {
  private ProtoH264() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public interface H264OrBuilder extends
      // @@protoc_insertion_point(interface_extends:H264)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <pre>
     *无线链路的信号质量百分比。(平台目前使用此参数作为遥控器信号质量)
     * </pre>
     *
     * <code>int32 size = 1;</code>
     * @return The size.
     */
    int getSize();

    /**
     * <code>bytes h264 = 2;</code>
     * @return The h264.
     */
    com.google.protobuf.ByteString getH264();
  }
  /**
   * Protobuf type {@code H264}
   */
  public  static final class H264 extends
      com.google.protobuf.GeneratedMessageLite<
          H264, H264.Builder> implements
      // @@protoc_insertion_point(message_implements:H264)
      H264OrBuilder {
    private H264() {
      h264_ = com.google.protobuf.ByteString.EMPTY;
    }
    public static final int SIZE_FIELD_NUMBER = 1;
    private int size_;
    /**
     * <pre>
     *无线链路的信号质量百分比。(平台目前使用此参数作为遥控器信号质量)
     * </pre>
     *
     * <code>int32 size = 1;</code>
     * @return The size.
     */
    @java.lang.Override
    public int getSize() {
      return size_;
    }
    /**
     * <pre>
     *无线链路的信号质量百分比。(平台目前使用此参数作为遥控器信号质量)
     * </pre>
     *
     * <code>int32 size = 1;</code>
     * @param value The size to set.
     */
    private void setSize(int value) {
      
      size_ = value;
    }
    /**
     * <pre>
     *无线链路的信号质量百分比。(平台目前使用此参数作为遥控器信号质量)
     * </pre>
     *
     * <code>int32 size = 1;</code>
     */
    private void clearSize() {
      
      size_ = 0;
    }

    public static final int H264_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString h264_;
    /**
     * <code>bytes h264 = 2;</code>
     * @return The h264.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getH264() {
      return h264_;
    }
    /**
     * <code>bytes h264 = 2;</code>
     * @param value The h264 to set.
     */
    private void setH264(com.google.protobuf.ByteString value) {
      java.lang.Class<?> valueClass = value.getClass();
  
      h264_ = value;
    }
    /**
     * <code>bytes h264 = 2;</code>
     */
    private void clearH264() {
      
      h264_ = getDefaultInstance().getH264();
    }

    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.apron.mobilesdk.state.ProtoH264.H264 parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return (Builder) DEFAULT_INSTANCE.createBuilder();
    }
    public static Builder newBuilder(com.apron.mobilesdk.state.ProtoH264.H264 prototype) {
      return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
    }

    /**
     * Protobuf type {@code H264}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.apron.mobilesdk.state.ProtoH264.H264, Builder> implements
        // @@protoc_insertion_point(builder_implements:H264)
        com.apron.mobilesdk.state.ProtoH264.H264OrBuilder {
      // Construct using com.apron.mobilesdk.state.ProtoH264.H264.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <pre>
       *无线链路的信号质量百分比。(平台目前使用此参数作为遥控器信号质量)
       * </pre>
       *
       * <code>int32 size = 1;</code>
       * @return The size.
       */
      @java.lang.Override
      public int getSize() {
        return instance.getSize();
      }
      /**
       * <pre>
       *无线链路的信号质量百分比。(平台目前使用此参数作为遥控器信号质量)
       * </pre>
       *
       * <code>int32 size = 1;</code>
       * @param value The size to set.
       * @return This builder for chaining.
       */
      public Builder setSize(int value) {
        copyOnWrite();
        instance.setSize(value);
        return this;
      }
      /**
       * <pre>
       *无线链路的信号质量百分比。(平台目前使用此参数作为遥控器信号质量)
       * </pre>
       *
       * <code>int32 size = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearSize() {
        copyOnWrite();
        instance.clearSize();
        return this;
      }

      /**
       * <code>bytes h264 = 2;</code>
       * @return The h264.
       */
      @java.lang.Override
      public com.google.protobuf.ByteString getH264() {
        return instance.getH264();
      }
      /**
       * <code>bytes h264 = 2;</code>
       * @param value The h264 to set.
       * @return This builder for chaining.
       */
      public Builder setH264(com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setH264(value);
        return this;
      }
      /**
       * <code>bytes h264 = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearH264() {
        copyOnWrite();
        instance.clearH264();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:H264)
    }
    @java.lang.Override
    @java.lang.SuppressWarnings({"unchecked", "fallthrough"})
    protected final java.lang.Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        java.lang.Object arg0, java.lang.Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.apron.mobilesdk.state.ProtoH264.H264();
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case BUILD_MESSAGE_INFO: {
            java.lang.Object[] objects = new java.lang.Object[] {
              "size_",
              "h264_",
            };
            java.lang.String info =
                "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0004\u0002\n" +
                "";
            return newMessageInfo(DEFAULT_INSTANCE, info, objects);
        }
        // fall through
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          com.google.protobuf.Parser<com.apron.mobilesdk.state.ProtoH264.H264> parser = PARSER;
          if (parser == null) {
            synchronized (com.apron.mobilesdk.state.ProtoH264.H264.class) {
              parser = PARSER;
              if (parser == null) {
                parser =
                    new DefaultInstanceBasedParser<com.apron.mobilesdk.state.ProtoH264.H264>(
                        DEFAULT_INSTANCE);
                PARSER = parser;
              }
            }
          }
          return parser;
      }
      case GET_MEMOIZED_IS_INITIALIZED: {
        return (byte) 1;
      }
      case SET_MEMOIZED_IS_INITIALIZED: {
        return null;
      }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:H264)
    private static final com.apron.mobilesdk.state.ProtoH264.H264 DEFAULT_INSTANCE;
    static {
      H264 defaultInstance = new H264();
      // New instances are implicitly immutable so no need to make
      // immutable.
      DEFAULT_INSTANCE = defaultInstance;
      com.google.protobuf.GeneratedMessageLite.registerDefaultInstance(
        H264.class, defaultInstance);
    }

    public static com.apron.mobilesdk.state.ProtoH264.H264 getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<H264> PARSER;

    public static com.google.protobuf.Parser<H264> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}