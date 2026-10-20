
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


public interface GetLocationQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      ExampleConnector,
      GetLocationQuery.Data,
      GetLocationQuery.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val location: Location?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Location(
  
    val name: String,
  
    val address: String?,
  
    val description: String?,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "GetLocation"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun GetLocationQuery.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.QueryRef<
    GetLocationQuery.Data,
    GetLocationQuery.Variables
  > =
  ref(
    
      GetLocationQuery.Variables(
        id=id,
  
      )
    
  )

public suspend fun GetLocationQuery.execute(

  
    
      id: java.util.UUID,
  fetchPolicy: com.google.firebase.dataconnect.QueryRef.FetchPolicy = com.google.firebase.dataconnect.QueryRef.FetchPolicy.PREFER_CACHE,
  

  ): com.google.firebase.dataconnect.QueryResult<
    GetLocationQuery.Data,
    GetLocationQuery.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute(fetchPolicy = fetchPolicy)


  public fun GetLocationQuery.flow(
    
      id: java.util.UUID,

  
    
    ): kotlinx.coroutines.flow.Flow<GetLocationQuery.Data> =
    ref(
        
          id=id,
  
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

