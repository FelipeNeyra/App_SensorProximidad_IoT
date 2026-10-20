
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


public interface ListLocationsQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      ListLocationsQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val locations: List<LocationsItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class LocationsItem(
  
    val name: String,
  
    val address: String?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "ListLocations"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun ListLocationsQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    ListLocationsQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun ListLocationsQuery.execute(

  
    fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    ListLocationsQuery.Data,
    Unit
  > =
  ref(
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun ListLocationsQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<ListLocationsQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

