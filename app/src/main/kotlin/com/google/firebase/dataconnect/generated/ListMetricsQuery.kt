
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


import kotlinx.coroutines.flow.filterNotNull as _flow_filterNotNull
import kotlinx.coroutines.flow.map as _flow_map


public interface ListMetricsQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      ListMetricsQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val metrics: List<MetricsItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class MetricsItem(
  
    val timestamp: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
    val value: Double,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "ListMetrics"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun ListMetricsQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    ListMetricsQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun ListMetricsQuery.execute(

  
    fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    ListMetricsQuery.Data,
    Unit
  > =
  ref(
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun ListMetricsQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<ListMetricsQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

