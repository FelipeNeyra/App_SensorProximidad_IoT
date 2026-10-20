
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



public interface DeleteMetricMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      DeleteMetricMutation.Data,
      DeleteMetricMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val metric_delete: MetricKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteMetric"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteMetricMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteMetricMutation.Data,
    DeleteMetricMutation.Variables
  > =
  ref(
    
      DeleteMetricMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteMetricMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteMetricMutation.Data,
    DeleteMetricMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


