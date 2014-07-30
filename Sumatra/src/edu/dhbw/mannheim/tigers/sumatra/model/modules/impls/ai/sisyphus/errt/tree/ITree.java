/*
 * *********************************************************
 * Copyright (c) 2009 - 2012, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Oct 8, 2012
 * Author(s): dirk
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.ai.sisyphus.errt.tree;


/**
 * every Tree used by ERRT should implement this interface
 * 
 * @author DirkK
 * 
 */
public interface ITree
{
	/**
	 * get the nearest node to a certain target node on the field
	 * 
	 * @param target
	 * @param allowRoot
	 * @return
	 */
	Node getNearest(Node target, boolean allowRoot);
	
	
	/**
	 * add a node to the tree
	 * 
	 * @param father the father/parent of the new node
	 * @param newNode the node which should be added to the tree
	 * @param isSuccessor
	 */
	void add(Node father, Node newNode, boolean isSuccessor);
	
	
	/**
	 * removes all nodes between this both nodes
	 * excl. the start and endNode
	 * 
	 * @param startNode
	 * @param endNode
	 * @param isSuccessor determines if the successor variable should be set, too
	 */
	void removeBetween(Node startNode, Node endNode, boolean isSuccessor);
	
	
	/**
	 * creates a double linked list
	 * 
	 * the successor variable is set and points to the child, which is the next on the path
	 */
	void makeDoubleLinkedList();
	
	
	/**
	 * get the root of the path
	 * 
	 * @return the node where the bot is
	 */
	Node getRoot();
	
	
}
