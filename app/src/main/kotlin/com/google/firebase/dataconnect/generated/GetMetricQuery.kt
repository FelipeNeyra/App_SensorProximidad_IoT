
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


public interface GetMetricQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      GetMetricQuery.Data,
      GetMetricQuery.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val metric: Metric?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Metric(
  
    val timestamp: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
    val value: Double,
  
    val unitOfMeasure: String?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetMetric"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun GetMetricQuery.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.QueryRef<
    GetMetricQuery.Data,
    GetMetricQuery.Variables
  > =
  ref(
    
      GetMetricQuery.Variables(
        id=id,
  
      )
    
  )

public suspend fun GetMetricQuery.execute(

  
    
      id: java.util.UUID,
  fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    GetMetricQuery.Data,
    GetMetricQuery.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun GetMetricQuery.flow(
    
      id: java.util.UUID,

  
    
    ): kotlinx.coroutines.flow.Flow<GetMetricQuery.Data> =
    ref(
        
          id=id,
  
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

