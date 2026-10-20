
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.google.firebase.dataconnect.generated



public interface UpdateAlertMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      UpdateAlertMutation.Data,
      UpdateAlertMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val resolvedAt: com.google.firebase.dataconnect.OptionalVariable<@kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp?>,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var resolvedAt: com.google.firebase.Timestamp?
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var resolvedAt: com.google.firebase.dataconnect.OptionalVariable<com.google.firebase.Timestamp?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var resolvedAt: com.google.firebase.Timestamp?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { resolvedAt = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,resolvedAt=resolvedAt,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val alert_update: AlertKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateAlert"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateAlertMutation.ref(
  
    id: java.util.UUID,

  
    block_: UpdateAlertMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateAlertMutation.Data,
    UpdateAlertMutation.Variables
  > =
  ref(
    
      UpdateAlertMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateAlertMutation.execute(

  
    
      id: java.util.UUID,

  
    block_: UpdateAlertMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateAlertMutation.Data,
    UpdateAlertMutation.Variables
  > =
  ref(
    
      id=id,
  
    block_
    
  ).execute()


