
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



public interface CreateMetricMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      CreateMetricMutation.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val metric_insert: MetricKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateMetric"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateMetricMutation.ref(
  
): com.google.firebase.dataconnect.MutationRef<
    CreateMetricMutation.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun CreateMetricMutation.execute(

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateMetricMutation.Data,
    Unit
  > =
  ref(
    
  ).execute()


