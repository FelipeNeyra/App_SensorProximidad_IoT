
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



public interface UpdateMetricMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      UpdateMetricMutation.Data,
      UpdateMetricMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val value: com.google.firebase.dataconnect.OptionalVariable<Double?>,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var value: Double?
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var value: com.google.firebase.dataconnect.OptionalVariable<Double?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var value: Double?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { value = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,value=value,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val metric_update: MetricKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateMetric"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateMetricMutation.ref(
  
    id: java.util.UUID,

  
    block_: UpdateMetricMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateMetricMutation.Data,
    UpdateMetricMutation.Variables
  > =
  ref(
    
      UpdateMetricMutation.Variables.build(
        id=id,
  
    block_
      )
    
  )

public suspend fun UpdateMetricMutation.execute(

  
    
      id: java.util.UUID,

  
    block_: UpdateMetricMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateMetricMutation.Data,
    UpdateMetricMutation.Variables
  > =
  ref(
    
      id=id,
  
    block_
    
  ).execute()


