
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



public interface DeleteSensorMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      ExampleConnector,
      DeleteSensorMutation.Data,
      DeleteSensorMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val sensor_delete: SensorKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteSensor"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteSensorMutation.ref(
  
    id: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteSensorMutation.Data,
    DeleteSensorMutation.Variables
  > =
  ref(
    
      DeleteSensorMutation.Variables(
        id=id,
  
      )
    
  )

public suspend fun DeleteSensorMutation.execute(

  
    
      id: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteSensorMutation.Data,
    DeleteSensorMutation.Variables
  > =
  ref(
    
      id=id,
  
    
  ).execute()


