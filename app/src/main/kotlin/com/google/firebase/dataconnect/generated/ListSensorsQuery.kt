
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


public interface ListSensorsQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      ListSensorsQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val sensors: List<SensorsItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class SensorsItem(
  
    val type: String,
  
    val status: String,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "ListSensors"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun ListSensorsQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    ListSensorsQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun ListSensorsQuery.execute(

  
    fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    ListSensorsQuery.Data,
    Unit
  > =
  ref(
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun ListSensorsQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<ListSensorsQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

