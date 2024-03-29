/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 23.11.2011
 * Author(s): Gero
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.view.commons.treetable;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;


/**
 * This is the {@link AbstractTableModel} implementation for the {@link javax.swing.JTable}-part of the
 * {@link JTreeTable}. It
 * therefore delegates the calls to the underlying {@link javax.swing.tree.TreeModel} (in {@link ITreeTableModel}).
 * 
 * @author Gero
 * @see JTreeTable
 */
public class TreeTableModelAdapter extends AbstractTableModel
{
	/**  */
	private static final long		serialVersionUID	= -6298333095243382630L;
	
	private final JTree				tree;
	private final ITreeTableModel	treeTableModel;
	
	
	/**
	 * @param treeTableModel
	 * @param tree
	 */
	public TreeTableModelAdapter(ITreeTableModel treeTableModel, JTree tree)
	{
		this.tree = tree;
		this.treeTableModel = treeTableModel;
		
		tree.addTreeExpansionListener(new TreeExpansionListener()
		{
			// Don't use fireTableRowsInserted() here; the selection model
			// would get updated twice.
			@Override
			public void treeExpanded(TreeExpansionEvent event)
			{
				fireTableDataChanged();
			}
			
			
			@Override
			public void treeCollapsed(TreeExpansionEvent event)
			{
				fireTableDataChanged();
			}
		});
		
		// Install a TreeModelListener that can update the table when
		// tree changes. We use delayedFireTableDataChanged as we can
		// not be guaranteed the tree will have finished processing
		// the event before us.
		treeTableModel.addTreeModelListener(new TreeModelListener()
		{
			@Override
			public void treeNodesChanged(TreeModelEvent e)
			{
				// Only one element changed
				if (e.getChildIndices().length == 0)
				{
					final int row = TreeTableModelAdapter.this.tree.getRowForPath(e.getTreePath());
					delayedFireTableRowsUpdated(row, row);
				} else
				{
					delayedFireTableDataChanged();
				}
			}
			
			
			@Override
			public void treeNodesInserted(TreeModelEvent e)
			{
				delayedFireTableDataChanged();
			}
			
			
			@Override
			public void treeNodesRemoved(TreeModelEvent e)
			{
				delayedFireTableDataChanged();
			}
			
			
			@Override
			public void treeStructureChanged(TreeModelEvent e)
			{
				delayedFireTableDataChanged();
			}
		});
	}
	
	
	// Wrappers, implementing TableModel interface.
	
	@Override
	public int getColumnCount()
	{
		return treeTableModel.getColumnCount();
	}
	
	
	@Override
	public String getColumnName(int column)
	{
		return treeTableModel.getColumnName(column);
	}
	
	
	@Override
	public Class<?> getColumnClass(int column)
	{
		return treeTableModel.getColumnClass(column);
	}
	
	
	@Override
	public int getRowCount()
	{
		return tree.getRowCount();
	}
	
	
	protected Object nodeForRow(int row)
	{
		final TreePath treePath = tree.getPathForRow(row);
		return treePath.getLastPathComponent();
	}
	
	
	@Override
	public Object getValueAt(int row, int column)
	{
		return treeTableModel.getValueAt(nodeForRow(row), column);
	}
	
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return treeTableModel.isCellEditable(nodeForRow(row), column);
	}
	
	
	@Override
	public void setValueAt(Object value, int row, int column)
	{
		treeTableModel.setValueAt(value, nodeForRow(row), column);
	}
	
	
	/**
	 * Invokes fireTableDataChanged after all the pending events have been
	 * processed. SwingUtilities.invokeLater is used to handle this.
	 */
	protected void delayedFireTableDataChanged()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableDataChanged();
			}
		});
	}
	
	
	/**
	 * Invokes {@link #fireTableRowsUpdated(int, int)} after all the pending events have been
	 * processed. SwingUtilities.invokeLater is used to handle this.
	 */
	protected void delayedFireTableRowsUpdated(final int firstRow, final int lastRow)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableRowsUpdated(firstRow, lastRow);
			}
		});
	}
}
