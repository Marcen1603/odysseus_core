/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
// Created by Hand from other generated file

package debs.challenge.msg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Descriptors.FieldDescriptor;

public final class TupleReaderMessages {

	static FieldDescriptor.Type[] inputSchema = new FieldDescriptor.Type[] {
			FieldDescriptor.Type.FIXED64, FieldDescriptor.Type.FIXED64,
			FieldDescriptor.Type.FIXED32, FieldDescriptor.Type.FIXED32,
			FieldDescriptor.Type.FIXED32, FieldDescriptor.Type.FIXED32,
			FieldDescriptor.Type.FIXED32, FieldDescriptor.Type.FIXED32,
			FieldDescriptor.Type.UINT32, FieldDescriptor.Type.UINT32,
			FieldDescriptor.Type.UINT32, FieldDescriptor.Type.UINT32,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL, FieldDescriptor.Type.BOOL,
			FieldDescriptor.Type.BOOL };

	List<Object> value = new ArrayList<Object>();

	private TupleReaderMessages() {
	}

	public static void registerAllExtensions(
			com.google.protobuf.ExtensionRegistry registry) {
	}

	public interface TupleOrBuilder extends
			com.google.protobuf.MessageOrBuilder {
	}

	public static final class TupleMessage extends
			com.google.protobuf.GeneratedMessage implements TupleOrBuilder {

		// Use TupleMessage.newBuilder() to construct.
		private TupleMessage(Builder builder) {
			super(builder);
		}

		private TupleMessage(boolean noInit) {
		}

		private static final TupleMessage defaultInstance;

		public static TupleMessage getDefaultInstance() {
			return defaultInstance;
		}

		@Override
		public TupleMessage getDefaultInstanceForType() {
			return defaultInstance;
		}

		public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
			return null;
		}

		@Override
		protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
			return null;
		}

		@Override
		public final boolean isInitialized() {
			return true;
		}

		@Override
		public void writeTo(com.google.protobuf.CodedOutputStream output)
				throws java.io.IOException {
			throw new RuntimeException(
					"Not Implemented. Use this class only for deserialization");
		}

		@Override
		public int getSerializedSize() {
			throw new RuntimeException(
					"Not Implemented. Use this class only for deserialization");

		}

		private static final long serialVersionUID = 0L;

		@java.lang.Override
		protected java.lang.Object writeReplace()
				throws java.io.ObjectStreamException {
			return super.writeReplace();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				com.google.protobuf.ByteString data)
				throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data).buildParsed();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				com.google.protobuf.ByteString data,
				com.google.protobuf.ExtensionRegistryLite extensionRegistry)
				throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data, extensionRegistry)
					.buildParsed();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				byte[] data)
				throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data).buildParsed();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				byte[] data,
				com.google.protobuf.ExtensionRegistryLite extensionRegistry)
				throws com.google.protobuf.InvalidProtocolBufferException {
			return newBuilder().mergeFrom(data, extensionRegistry)
					.buildParsed();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				java.io.InputStream input) throws java.io.IOException {
			return newBuilder().mergeFrom(input).buildParsed();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				java.io.InputStream input,
				com.google.protobuf.ExtensionRegistryLite extensionRegistry)
				throws java.io.IOException {
			return newBuilder().mergeFrom(input, extensionRegistry)
					.buildParsed();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseDelimitedFrom(
				java.io.InputStream input) throws java.io.IOException {
			Builder builder = newBuilder();
			if (builder.mergeDelimitedFrom(input)) {
				return builder.buildParsed();
			} else {
				return null;
			}
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseDelimitedFrom(
				java.io.InputStream input,
				com.google.protobuf.ExtensionRegistryLite extensionRegistry)
				throws java.io.IOException {
			Builder builder = newBuilder();
			if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
				return builder.buildParsed();
			} else {
				return null;
			}
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				com.google.protobuf.CodedInputStream input)
				throws java.io.IOException {
			return newBuilder().mergeFrom(input).buildParsed();
		}

		public static debs.challenge.msg.TupleReaderMessages.TupleMessage parseFrom(
				com.google.protobuf.CodedInputStream input,
				com.google.protobuf.ExtensionRegistryLite extensionRegistry)
				throws java.io.IOException {
			return newBuilder().mergeFrom(input, extensionRegistry)
					.buildParsed();
		}

		public static Builder newBuilder() {
			return Builder.create();
		}

		@Override
		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(
				debs.challenge.msg.TupleReaderMessages.TupleMessage prototype) {
			return newBuilder().mergeFrom(prototype);
		}

		@Override
		public Builder toBuilder() {
			return newBuilder(this);
		}

		@java.lang.Override
		protected Builder newBuilderForType(
				com.google.protobuf.GeneratedMessage.BuilderParent parent) {
			Builder builder = new Builder(parent);
			return builder;
		}

		public static final class Builder extends
				com.google.protobuf.GeneratedMessage.Builder<Builder> implements
				debs.challenge.msg.TupleReaderMessages.TupleOrBuilder {

			public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
				return null;
			}

			@Override
			protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
				return null;
			}

			// Construct using
			// debs.challenge.msg.CManufacturingMessages.CDataPoint.newBuilder()
			private Builder() {
				maybeForceBuilderInitialization();
			}

			private Builder(BuilderParent parent) {
				super(parent);
				maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
				}
			}

			private static Builder create() {
				return new Builder();
			}

			@Override
			public Builder clear() {
				super.clear();
				return this;
			}

			@Override
			public Builder clone() {
				return create().mergeFrom(buildPartial());
			}

			@Override
			public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
				return debs.challenge.msg.TupleReaderMessages.TupleMessage
						.getDescriptor();
			}

			@Override
			public debs.challenge.msg.TupleReaderMessages.TupleMessage getDefaultInstanceForType() {
				return debs.challenge.msg.TupleReaderMessages.TupleMessage
						.getDefaultInstance();
			}

			@Override
			public debs.challenge.msg.TupleReaderMessages.TupleMessage build() {
				debs.challenge.msg.TupleReaderMessages.TupleMessage result = buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				}
				return result;
			}

			private debs.challenge.msg.TupleReaderMessages.TupleMessage buildParsed()
					throws com.google.protobuf.InvalidProtocolBufferException {
				debs.challenge.msg.TupleReaderMessages.TupleMessage result = buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result)
							.asInvalidProtocolBufferException();
				}
				return result;
			}

			@Override
			public debs.challenge.msg.TupleReaderMessages.TupleMessage buildPartial() {
				debs.challenge.msg.TupleReaderMessages.TupleMessage result = new debs.challenge.msg.TupleReaderMessages.TupleMessage(
						this);

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// TODO: COPY Elements
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				onBuilt();
				return result;
			}

			@Override
			public Builder mergeFrom(com.google.protobuf.Message other) {
				if (other instanceof debs.challenge.msg.TupleReaderMessages.TupleMessage) {
					return mergeFrom((debs.challenge.msg.TupleReaderMessages.TupleMessage) other);
				} else {
					super.mergeFrom(other);
					return this;
				}
			}

			public Builder mergeFrom(
					debs.challenge.msg.TupleReaderMessages.TupleMessage other) {
				if (other == debs.challenge.msg.TupleReaderMessages.TupleMessage
						.getDefaultInstance())
					return this;
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// TODO: SET TUPLE
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				return this;
			}

			@Override
			public final boolean isInitialized() {

				return true;
			}

			@Override
			public Builder mergeFrom(
					com.google.protobuf.CodedInputStream input,
					com.google.protobuf.ExtensionRegistryLite extensionRegistry)
					throws java.io.IOException {
				com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet
						.newBuilder(this.getUnknownFields());
				while (true) {
					int tag = input.readTag();

					if (tag == 0) {
						this.setUnknownFields(unknownFields.build());
						onChanged();
						return this;
					}

					int pos = tag >> 3;
					System.err.println(pos + " " + tag);
					if (pos <= inputSchema.length) {
						System.err.println(readValue(inputSchema[pos], input));
					}
				}
			}

			private Object readValue(FieldDescriptor.Type inputType,
					CodedInputStream inputStream) throws IOException {
				Object ret = null;

				switch (inputType) {
				case INT32:
					ret = inputStream.readInt32();
					break;
				case INT64:
					ret = inputStream.readInt64();
					break;
				case UINT32:
					ret = inputStream.readUInt32();
					break;
				case UINT64:
					ret = inputStream.readUInt64();
					break;
				case SINT32:
					ret = inputStream.readSInt32();
					break;
				case SINT64:
					ret = inputStream.readSInt64();
					break;
				case FIXED32:
					ret = inputStream.readFixed32();
					break;
				case FIXED64:
					ret = inputStream.readFixed64();
					break;
				case SFIXED32:
					ret = inputStream.readSFixed32();
					break;
				case SFIXED64:
					ret = inputStream.readSFixed64();
					break;
				case FLOAT:
					ret = inputStream.readFloat();
					break;
				case DOUBLE:
					ret = inputStream.readDouble();
					break;
				case BOOL:
					ret = inputStream.readBool();
					break;
				case STRING:
					ret = inputStream.readString();
					break;
				case BYTES:
					ret = inputStream.readBytes();
					break;
				}
				return ret;
			}

			// @@protoc_insertion_point(builder_scope:debs.challenge.msg.CDataPoint)
		}

		static {
			defaultInstance = new TupleMessage(true);
		}

		// @@protoc_insertion_point(class_scope:debs.challenge.msg.CDataPoint)
	}

	public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
		return descriptor;
	}

	private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

	// @@protoc_insertion_point(outer_class_scope)
}
