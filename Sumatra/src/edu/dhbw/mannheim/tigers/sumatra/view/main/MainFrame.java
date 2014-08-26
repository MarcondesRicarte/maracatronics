/*
 * *********************************************************
 * Copyright (c) 2010 DHBW Mannheim - Tigers Mannheim
 * Project: Sumatra
 * Date: Jul 27, 2010
 * Authors:
 * Bernhard Perun <bernhard.perun@googlemail.com>
 * *********************************************************
 */

package edu.dhbw.mannheim.tigers.sumatra.view.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.RootWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;

import org.apache.log4j.Logger;

import edu.dhbw.mannheim.tigers.sumatra.model.SumatraModel;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.types.AAgent;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.types.ABotManager;
import edu.dhbw.mannheim.tigers.sumatra.view.commons.ConfigControlMenu;
import edu.dhbw.mannheim.tigers.sumatra.view.main.toolbar.ToolBar;
import edu.dhbw.mannheim.tigers.sumatra.views.ASumatraView;
import edu.dhbw.mannheim.tigers.sumatra.views.ESumatraViewType;


/**
 * The Sumatra-main view with all available JComponents as dockable views :).
 */
public class MainFrame extends JFrame implements IMainFrame
{
	// ---------------------------------------------------------------
	// --- instance vars ---------------------------------------------
	// ---------------------------------------------------------------
	
	// Logger
	private static final Logger						log						= Logger.getLogger(MainFrame.class.getName());
	
	/**  */
	private static final long							serialVersionUID		= -6858464942004450029L;
	
	
	private final List<IMainFrameObserver>			observers				= new ArrayList<IMainFrameObserver>();
	
	private final List<JRadioButtonMenuItem>		layoutItems				= new ArrayList<JRadioButtonMenuItem>();
	
	
	// --- infonode-framework variables ---
	/**
	 * The one and only root window
	 */
	private RootWindow									rootWindow;
	
	// --- menu stuff ---
	private JMenuBar										menuBar					= null;
	private JMenu											viewsMenu				= null;
	private JMenu											layoutMenu				= null;
	private JMenu											moduliMenu				= null;
	private JMenu											lookAndFeelMenu		= null;
	private final ConfigControlMenu					botManagerConfigMenu	= new ConfigControlMenu("Botmanager",
																									ABotManager.KEY_BOTMANAGER_CONFIG);
	private final ConfigControlMenu					geomConfigMenu			= new ConfigControlMenu("Geometry",
																									AAgent.KEY_GEOMETRY_CONFIG);
	
	private final List<ASumatraView>					views						= new ArrayList<ASumatraView>();
	private final Map<ASumatraView, List<JMenu>>	customMenuMap			= new Hashtable<ASumatraView, List<JMenu>>();
	
	private ToolBar										toolBar;
	
	
	// ---------------------------------------------------------------
	// --- constructor(s) --------------------------------------------
	// ---------------------------------------------------------------
	
	/**
	 * Constructor of GuiView.
	 */
	public MainFrame()
	{
		log.trace("creating");
		// --- create the main window with the default layout (file: default.ly) ---
		createRootWindow();
		log.trace("root window created");
		
		// --- usual Swing initialization of the view ---
		showFrame();
		log.trace("created");
	}
	
	
	// ---------------------------------------------------------------
	// --- important-methods for every day use -----------------------
	// ---------------------------------------------------------------
	
	@Override
	public void addObserver(final IMainFrameObserver o)
	{
		synchronized (observers)
		{
			observers.add(o);
		}
	}
	
	
	@Override
	public void removeObserver(final IMainFrameObserver o)
	{
		synchronized (observers)
		{
			observers.remove(o);
		}
	}
	
	
	@Override
	public void loadLayout(final String path)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final File f = new File(path);
				final String filename = f.getName();
				log.trace("Loading layout file " + filename);
				
				try
				{
					// --- Load the layout ---
					final ObjectInputStream in1 = new ObjectInputStream(new FileInputStream(f));
					
					try
					{
						rootWindow.read(in1, true);
					} catch (final NullPointerException npe)
					{
						log.warn("Seems as if a view stored in the config is not available!", npe);
					}
					in1.close();
					
					// select RadioButton in layoutMenu
					for (final JRadioButtonMenuItem item : layoutItems)
					{
						final String itemName = item.getText();
						if (itemName.equals(filename))
						{
							item.setSelected(true);
						}
					}
				} catch (final IOException e1)
				{
					log.warn("Can't load layout: " + e1.getMessage());
				}
			}
		});
	}
	
	
	@Override
	public void saveLayout(final String filename)
	{
		// --- save layout to file ---
		ObjectOutputStream out = null;
		try
		{
			final FileOutputStream fileStream = new FileOutputStream(filename);
			out = new ObjectOutputStream(fileStream);
			rootWindow.write(out, false);
			
		} catch (final FileNotFoundException err)
		{
			log.error("Can't save layout:" + err.getMessage());
		} catch (final IOException err)
		{
			log.error("Can't save layout:" + err.getMessage());
		}
		if (out != null)
		{
			try
			{
				out.close();
			} catch (IOException err)
			{
				// what should we do?
			}
		}
	}
	
	
	@Override
	public void setMenuLayoutItems(final List<String> names)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				// remove all layout items from menu
				for (final JRadioButtonMenuItem item : layoutItems)
				{
					layoutMenu.remove(item);
				}
				
				layoutItems.clear();
				
				// --- buttonGroup for layout-files ---
				final ButtonGroup group = new ButtonGroup();
				for (final String name : names)
				{
					final JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
					group.add(item);
					item.addActionListener(new LoadLayout(name));
					layoutItems.add(item);
				}
				
				int pos = 3;
				for (final JRadioButtonMenuItem item : layoutItems)
				{
					layoutMenu.insert(item, pos++);
				}
			}
		});
	}
	
	
	@Override
	public void setMenuModuliItems(final List<String> names)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				moduliMenu.removeAll();
				
				final ButtonGroup group = new ButtonGroup();
				
				for (final String name : names)
				{
					final JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
					item.addActionListener(new LoadConfig(name));
					group.add(item);
					moduliMenu.add(item);
				}
			}
		});
	}
	
	
	/**
	 * @param enabled
	 */
	public void setModuliMenuEnabled(final boolean enabled)
	{
		for (int i = 0; i < moduliMenu.getItemCount(); i++)
		{
			moduliMenu.getItem(i).setEnabled(enabled);
		}
		for (int i = 0; i < botManagerConfigMenu.getConfigMenu().getItemCount(); i++)
		{
			JMenuItem item = botManagerConfigMenu.getConfigMenu().getItem(i);
			if (item != null)
			{
				item.setEnabled(enabled);
			}
		}
		for (int i = 0; i < geomConfigMenu.getConfigMenu().getItemCount(); i++)
		{
			JMenuItem item = geomConfigMenu.getConfigMenu().getItem(i);
			if (item != null)
			{
				item.setEnabled(enabled);
			}
		}
	}
	
	
	@Override
	public void selectModuliItem(final String name)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				// select RadioButton in moduliMenu
				for (int i = 0; i < moduliMenu.getItemCount(); i++)
				{
					final JRadioButtonMenuItem item = (JRadioButtonMenuItem) moduliMenu.getItem(i);
					if (item.getText().equals(name))
					{
						item.setSelected(true);
					}
				}
			}
		});
	}
	
	
	@Override
	public void selectLayoutItem(final String name)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				for (final JRadioButtonMenuItem item : layoutItems)
				{
					if (item.getText().equals(name))
					{
						item.setSelected(true);
					}
				}
			}
		});
	}
	
	
	/**
	 * @param name
	 */
	public void selectLookAndFeelItem(final String name)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				for (int i = 0; i < lookAndFeelMenu.getItemCount(); i++)
				{
					final JRadioButtonMenuItem item = (JRadioButtonMenuItem) lookAndFeelMenu.getItem(i);
					if (item.getText().equals(name))
					{
						item.setSelected(true);
					}
				}
			}
		});
	}
	
	
	@Override
	public void addView(final ASumatraView view)
	{
		views.add(view);
	}
	
	
	@Override
	public void setLookAndFeel(final String lafName)
	{
		final JFrame frame = this;
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				// update visible components
				final int state = getExtendedState();
				SwingUtilities.updateComponentTreeUI(frame);
				setExtendedState(state);
				
				// update menu
				for (int i = 0; i < lookAndFeelMenu.getItemCount(); i++)
				{
					final JRadioButtonMenuItem item = (JRadioButtonMenuItem) lookAndFeelMenu.getItem(i);
					if (item.getText().equals(lafName))
					{
						item.setSelected(true);
					}
				}
				
				// update all views (including non-visible)
				for (final ASumatraView view : views)
				{
					if (view.isInitialized())
					{
						SwingUtilities.updateComponentTreeUI(view.getComponent());
					}
				}
			}
		});
	}
	
	
	/**
	 * @return
	 */
	public ToolBar getToolbar()
	{
		return toolBar;
	}
	
	
	private void removeFromCustomMenu(final List<JMenu> menus)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				for (final JMenu menu : menus)
				{
					menuBar.remove(menu);
				}
				
				menuBar.repaint();
			}
		});
	}
	
	
	private void addToCustomMenu(final List<JMenu> menus, final ASumatraView view)
	{
		if (menus != null)
		{
			if (customMenuMap.containsKey(view))
			{
				removeFromCustomMenu(customMenuMap.get(view));
			}
			
			customMenuMap.put(view, menus);
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					for (final JMenu menu : menus)
					{
						menuBar.add(menu);
					}
				}
			});
		}
	}
	
	
	/**
	 * Initializes the frame and shows it.
	 */
	private void showFrame()
	{
		// --- init windowlistener ---
		addWindowListener(new Exit());
		
		// --- some adjustments ---
		setLayout(new BorderLayout());
		// this.setLocation(0, 0);
		this.setSize(new Dimension(800, 600));
		setTitle("Maracatronics V " + SumatraModel.getVersion());
		URL kralleUrl = ClassLoader.getSystemResource("icon.png");
		if (kralleUrl != null)
		{
			ImageIcon icon = new ImageIcon(kralleUrl);
			setIconImage(icon.getImage());
		}
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// --- add/set components ---
		toolBar = new ToolBar();
		this.add(toolBar.getToolbar(), BorderLayout.NORTH);
		this.add(rootWindow, BorderLayout.CENTER);
		setJMenuBar(createMenuBar());
		
		// --- maximize ---
		// setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
		
		// --- set visible ---
		// setVisible(true);
	}
	
	
	// ---------------------------------------------------------------
	// --- unimportant-methods for every day use (framework-things) --
	// ---------------------------------------------------------------
	
	/**
	 * Creates the root window and the views.
	 */
	private void createRootWindow()
	{
		ViewMap viewMap = new ViewMap();
		// The mixed view map makes it easy to mix static and dynamic views inside the same root window
		MixedViewHandler handler = new MixedViewHandler(viewMap, new ViewSerializer()
		{
			@Override
			public void writeView(final View view, final ObjectOutputStream out) throws IOException
			{
				String title = view.getTitle();
				for (ESumatraViewType viewType : ESumatraViewType.values())
				{
					if (viewType.getTitle().equals(title))
					{
						out.writeInt(viewType.getId());
						return;
					}
				}
			}
			
			
			@Override
			public View readView(final ObjectInputStream in) throws IOException
			{
				int id = in.readInt();
				
				for (ASumatraView sumatraView : views)
				{
					if (sumatraView.getType().getId() == id)
					{
						return sumatraView.getView();
					}
				}
				log.error("There is no view with id " + id + ". Can not load.");
				return null;
			}
		});
		
		// --- create the RootWindow with MixedHandler ---
		rootWindow = DockingUtil.createRootWindow(viewMap, handler, true);
		
		// --- add a listener which updates the menus when a window is closing or closed.
		rootWindow.addListener(new ViewUpdater());
		
		/*
		 * In this properties object the modified property values for close buttons etc. are stored. This object is
		 * cleared
		 * when the theme is changed.
		 */
		RootWindowProperties properties = new RootWindowProperties();
		
		// --- set gradient theme. The theme properties object is the super object of our properties object, which
		// means our property value settings will override the theme values ---
		properties.addSuperObject(new ShapedGradientDockingTheme().getRootWindowProperties());
		
		// --- our properties object is the super object of the root window properties object, so all property values of
		// the
		// theme and in our property object will be used by the root window ---
		rootWindow.getRootWindowProperties().addSuperObject(properties);
		
		// --- enable the bottom window bar ---
		rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
	}
	
	
	/**
	 * Creates the frame menu bar.
	 * 
	 * @return the menu bar
	 */
	private JMenuBar createMenuBar()
	{
		// Views menu
		viewsMenu = new JMenu("Views");
		
		// File Menu
		JMenu sumatraMenu = new JMenu("Sumatra");
		
		JMenuItem aboutMenuItem;
		JMenuItem exitMenuItem;
		
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new Exit());
		exitMenuItem.setToolTipText("Exits the application");
		
		// fullscreen stuff
		final JMenu fsMenu = new JMenu("Fullscreen");
		
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice[] gds = ge.getScreenDevices();
		for (final GraphicsDevice gd : gds)
		{
			final JMenuItem item = new JMenuItem(gd.toString());
			item.addActionListener(new SetFullscreen(gd));
			fsMenu.add(item);
		}
		
		final JMenuItem windowMode = new JMenuItem("Window mode");
		windowMode.addActionListener(new SetFullscreen(null));
		fsMenu.add(windowMode);
		
		final JMenuItem shortcutMenuItem = new JMenuItem("Shortcuts");
		shortcutMenuItem.addActionListener(new ShortcutActionListener());
		
		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(new AboutBox());
		aboutMenuItem.setToolTipText("Information about Sumatra");
		
		sumatraMenu.add(fsMenu);
		sumatraMenu.add(shortcutMenuItem);
		sumatraMenu.add(aboutMenuItem);
		sumatraMenu.add(exitMenuItem);
		
		
		// layout menu
		layoutMenu = new JMenu("Layout");
		
		JMenuItem saveLayoutItem;
		JMenuItem deleteLayoutItem;
		
		saveLayoutItem = new JMenuItem("Save layout");
		saveLayoutItem.addActionListener(new SaveLayout());
		saveLayoutItem.setToolTipText("Saves current layout to file");
		deleteLayoutItem = new JMenuItem("Delete layout");
		deleteLayoutItem.addActionListener(new DeleteLayout());
		deleteLayoutItem.setToolTipText("Deletes current layout");
		
		layoutMenu.add(saveLayoutItem);
		layoutMenu.add(deleteLayoutItem);
		
		layoutMenu.addSeparator();
		layoutMenu.addSeparator();
		
		layoutMenu.add(new JMenuItem("Refresh layouts")).addActionListener(new RefreshLayoutItems());
		
		// moduli menu
		moduliMenu = new JMenu("Moduli");
		
		// look and feel menu
		final ButtonGroup group = new ButtonGroup();
		lookAndFeelMenu = new JMenu("Look & Feel");
		final LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		for (final LookAndFeelInfo info : lafs)
		{
			final JRadioButtonMenuItem item = new JRadioButtonMenuItem(info.getName());
			item.addActionListener(new SetLookAndFeel(info));
			group.add(item);
			lookAndFeelMenu.add(item);
			if (info.getClassName().equals(UIManager.getSystemLookAndFeelClassName()))
			{
				item.setSelected(true);
			}
		}
		
		menuBar = new JMenuBar();
		menuBar.add(sumatraMenu);
		menuBar.add(moduliMenu);
		menuBar.add(layoutMenu);
		menuBar.add(viewsMenu);
		menuBar.add(lookAndFeelMenu);
		menuBar.add(Box.createHorizontalStrut(50));
		menuBar.add(botManagerConfigMenu.getConfigMenu());
		menuBar.add(geomConfigMenu.getConfigMenu());
		
		return menuBar;
	}
	
	
	/**
	 * Must be called after adding views
	 */
	public void updateViewMenu()
	{
		for (ASumatraView view : views)
		{
			if (!view.isInitialized())
			{
				continue;
			}
			final List<JMenu> menu = view.getSumatraView().getCustomMenus();
			addToCustomMenu(menu, view);
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				for (int i = 0; i < viewsMenu.getItemCount(); i++)
				{
					final ActionListener[] listener = viewsMenu.getItem(i).getActionListeners();
					for (final ActionListener l : listener)
					{
						viewsMenu.getItem(i).removeActionListener(l);
					}
				}
				
				viewsMenu.removeAll();
				
				for (ASumatraView sumatraView : views)
				{
					final JMenuItem item = new JMenuItem(sumatraView.getType().getTitle());
					
					item.addActionListener(new RestoreView(sumatraView));
					
					if (sumatraView.isInitialized())
					{
						View view = sumatraView.getView();
						item.setEnabled(view.getRootWindow() == null);
					}
					viewsMenu.add(item);
				}
			}
		});
	}
	
	// -------------------------
	// --- ActionListener ------
	// -------------------------
	
	
	protected class SaveLayout implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			synchronized (observers)
			{
				for (final IMainFrameObserver o : observers)
				{
					o.onSaveLayout();
				}
			}
		}
	}
	
	protected class DeleteLayout implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			synchronized (observers)
			{
				for (final IMainFrameObserver o : observers)
				{
					o.onDeleteLayout();
				}
			}
		}
	}
	
	protected class LoadLayout implements ActionListener
	{
		private final String	layoutName;
		
		
		/**
		 * @param name
		 */
		public LoadLayout(final String name)
		{
			layoutName = name;
		}
		
		
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			synchronized (observers)
			{
				for (final IMainFrameObserver o : observers)
				{
					o.onLoadLayout(layoutName);
				}
			}
		}
	}
	
	protected class AboutBox implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			synchronized (observers)
			{
				for (final IMainFrameObserver o : observers)
				{
					o.onAbout();
				}
			}
		}
	}
	
	protected class RestoreView implements ActionListener
	{
		private final ASumatraView	sumatraView;
		
		
		/**
		 * @param v
		 */
		public RestoreView(final ASumatraView v)
		{
			sumatraView = v;
		}
		
		
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			final JMenuItem item = (JMenuItem) e.getSource();
			
			sumatraView.ensureInitialized();
			sumatraView.getView().restoreFocus();
			DockingUtil.addWindow(sumatraView.getView(), rootWindow);
			item.setEnabled(false);
		}
	}
	
	protected class LoadConfig implements ActionListener
	{
		private final String	configName;
		
		
		/**
		 * @param c
		 */
		public LoadConfig(final String c)
		{
			configName = c;
		}
		
		
		@Override
		public void actionPerformed(final ActionEvent arg0)
		{
			SumatraModel.getInstance().setCurrentModuliConfig(configName);
		}
	}
	
	protected class RefreshLayoutItems implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			synchronized (observers)
			{
				for (final IMainFrameObserver o : observers)
				{
					o.onRefreshLayoutItems();
				}
			}
		}
	}
	
	protected class SetLookAndFeel implements ActionListener
	{
		private final LookAndFeelInfo	info;
		
		
		/**
		 * @param i
		 */
		public SetLookAndFeel(final LookAndFeelInfo i)
		{
			info = i;
		}
		
		
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			synchronized (observers)
			{
				for (final IMainFrameObserver o : observers)
				{
					o.onSelectLookAndFeel(info);
				}
			}
		}
	}
	
	protected class SetFullscreen implements ActionListener
	{
		private final GraphicsDevice	gd;
		
		
		/**
		 * @param gd
		 */
		public SetFullscreen(final GraphicsDevice gd)
		{
			this.gd = gd;
		}
		
		
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			synchronized (observers)
			{
				for (final IMainFrameObserver o : observers)
				{
					o.onSetFullscreen(gd);
				}
			}
		}
	}
	
	private static class ShortcutActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			new ShortcutsDialog().setVisible(true);
		}
	}
	
	protected class Exit extends WindowAdapter implements ActionListener
	{
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			exit();
		}
		
		
		@Override
		public void windowClosing(final WindowEvent w)
		{
			exit();
		}
		
		
		private void exit()
		{
			for (final IMainFrameObserver o : observers)
			{
				o.onExit();
			}
		}
	}
	
	protected class ViewUpdater extends DockingWindowAdapter
	{
		@Override
		public void windowAdded(final DockingWindow addedToWindow, final DockingWindow addedWindow)
		{
			updateViewMenu();
		}
		
		
		@Override
		public void windowRemoved(final DockingWindow removedFromWindow, final DockingWindow removedWindow)
		{
			updateViewMenu();
		}
		
		
		@Override
		public void windowShown(final DockingWindow window)
		{
			/*
			 * It is a completely undocumented feature(?) that the
			 * window title consists of a comma separated list if multiple
			 * windows get shown at once. This usually happens when loading
			 * a layout.
			 */
			for (final ASumatraView view : views)
			{
				if (!view.isInitialized())
				{
					continue;
				}
				final List<JMenu> menu = view.getSumatraView().getCustomMenus();
				addToCustomMenu(menu, view);
				
				view.getSumatraView().onShown();
			}
		}
		
		
		@Override
		public void windowHidden(final DockingWindow window)
		{
			final String titles[] = window.getTitle().split(",");
			for (String title : titles)
			{
				title = title.trim();
				
				for (final ASumatraView view : views)
				{
					if (view.getType().getTitle().equals(title))
					{
						final List<JMenu> menu = customMenuMap.remove(view);
						if (menu != null)
						{
							removeFromCustomMenu(menu);
						}
						
						view.getSumatraView().onHidden();
					}
				}
			}
		}
		
		
		@Override
		public void viewFocusChanged(final View previous, final View focused)
		{
			if (previous != null)
			{
				for (final ASumatraView view : views)
				{
					if (view.getType().getTitle().equals(previous.getTitle()))
					{
						view.getSumatraView().onFocusLost();
					}
				}
			}
			
			if (focused != null)
			{
				for (final ASumatraView view : views)
				{
					if (view.getType().getTitle().equals(focused.getTitle()))
					{
						view.getSumatraView().onFocused();
					}
				}
			}
		}
		
	}
	
	
	/**
	 * @return the views
	 */
	public final List<ASumatraView> getViews()
	{
		return views;
	}
	
}
