package io.corbel.lib.mongo.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This repository interface adds functionality for partial updates.
 * 
 * @author Alexander De Leon
 * 
 */
public interface PartialUpdateRepository<E, ID extends Serializable> extends MongoRepository<E, ID> {

	/**
	 * Partially updates (if exists) the object with the specified id, using all non-null values in the data parameter.
	 * Optionally a list of fieldsToDelete can be specified to unset those fields in the DB.
	 * 
	 * @param id
	 *            The identifier of the object to update
	 * @param data
	 *            An object containing the values that most be updated
	 * @param fieldsToDelete
	 *            An optional list of fields to unset
	 * @return true if such object is found and updated
	 */
	public boolean patch(ID id, E data, String... fieldsToDelete);

	/**
	 * Partially updates (if exists) the object identified by the the identifier field expected in the data object. The
	 * object will be updated using all non-null values in the data parameter. Optionally a list of fieldsToDelete can
	 * be specified to unset those fields in the DB.
	 *
	 * @param data
	 *            An object containing the values that most be updated
	 * @param fieldsToDelete
	 *            An optional list of fields to unset
	 * @return true if such object is found and updated
	 */
	public boolean patch(E data, String... fieldsToDelete);


    /**
     * Partially updates (or insert) the object with the specified id, using all non-null values in the data parameter.
     *
     * @param id
     *            The identifier of the object to update
     * @param data
     *            An object containing the values
     * @return true if such object is found and updated
     */
    public boolean upsert(ID id, E data);

}
