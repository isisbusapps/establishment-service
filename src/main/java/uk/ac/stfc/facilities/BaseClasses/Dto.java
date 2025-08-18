package uk.ac.stfc.facilities.BaseClasses;

/**
 * Marker interface for DTOs
 *
 * DTOs should hold only data transferred between the client and server, any other data from the model should be
 * discarded in conversion.
 *
 * @apiNote DTOs are used to transfer data via SOAP, serialized DTOs via REST
 */
public interface Dto {
}
