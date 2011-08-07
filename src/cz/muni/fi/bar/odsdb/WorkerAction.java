/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.bar.odsdb;

/**
 * Defines three types of Worker Action
 * GET - get the particular type of media
 * GET_ALL - get all media in the file
 * SAVE - store the current database
 * @author bar
 */
public enum WorkerAction {
    GET, GET_ALL, SAVE
}
