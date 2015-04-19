CLIENT

PUBLIC
* Set local data dir path --> delegate to DataSource m_dataSource
* Sync local with a callback when complete (SyncNotifier)
* Sync remote with a callback when complete (SyncNotifier)
* hasRootView(): return null if there's no root view OR views are broken
* In a view, we need: background: byte[], a list of items, items count
* In an item, we need: name, image: byte[], nextViewKey, sendingKey, sendingValue
* Provide a view from a given view key: return null if there's no such view
* Send action(key, value) to server

PRIVATE
* When sync finish, data are stored in m_data: Map<String, byte[]>
* Before sync, m_data is cleared
* Method buildViews(): void will process data (by using getData()) to create views/items, begin with root view, if there's something broken, m_data will be cleared
* Method getData(key: String): byte[] will look at m_data first and then local dir, return null if no such key

void buildViews() {
	if (!makeView(ROOT_VIEW_KEY)) {
		m_data.clear(); // so hasRootView() will return false;
	}
}

boolean processView(key: String) {
	byte[] data = getData(key);
	if (data == null) return false;
	String json = new String(data);
	// Parse json to object: obj
	ViewJson obj = ViewJson.create(json);
	if (obj == null) return false;

	byte[] background = getData(obj.getBackgroundImageKey());
	if (background == null) return false;

	

	int itemCount = obj.getItemCount();
	for (int i = 0; i < itemCount; i++) {
		Item item = obj.getItem(i);
		
	}
	m_data.add(key, view);
}

JSON {
	title: "xxx",
	items: [
		{
			imageKey: "xxx",
			text: "xxx",
			nextViewKey: "xxx" // optional
			sendingData: {
				key: "xxx",
				value: "xxx"
			} // optional
		}
	],
	backImageKey: "xxx"
}


class View {
	public static View createFromJson(String json, DataSource dataSource);
	
	public void byte[] getBackground();
	public int getItemCount();
	
	public Item getItem(int index);
}

class Item {
	public String name;
	public byte[] image;
	public String nextViewKey;
	public String sendingKey;
	public String sendingValue;
	public Item setName(String name) { this.name = name; return this; }
}

class CAwear {

	boolean makeView(String key) {
		byte[] data = m_dataSource.get(key);
		if (data == null) return false;
		String json = new String(data);
		
		View view = View.createFromJson(json);
		if (view == null) return false;

		int count = view.getItemCount();
		for (int i = 0; i < count; i++) {
			Item item = view.getItem(i);
			if (item.nextViewKey != null) {
				if (!makeView(item.nextViewKey) return false;
			}
		}
		
		m_dataSource.add(key, view);
		return true;
	}
}

class DataSource {
	public DataSource(String dirPath);
}









sync data from mobile
show the root view: what is root view?

key = "view" => root view

key = "view_events" => value = "{
	title: "Events"
	items: [
		{
			imageKey: "img_chris",
			text: "Hello Chrismas",
			nextViewKey: "view_events_chrismas" // optional
			sendingData: {
				key: "",
				value: ""
			} // optional
		}
	],
	backItem: {
		imageKey: "img_back"
	}
}"


Bitmap dummyBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
		for (int i = 0; i < 32; i++) for (int j = 0; j < 32; j++) {
			dummyBitmap.setPixel(i, j, 0xFFFF00FF);
		}

		// Mock data
		ViewData vd;
		m_viewDataList.clear();

		// 1
		vd = new ViewData();
		vd.addItem(new ViewItemData(dummyBitmap, "Back", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Cong hoa xa hoi chu nghia Viet Nam", "view_loadout", null));
		vd.addItem(new ViewItemData(dummyBitmap, "Hello World 2", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Hello World 3", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Hello World 4", null, null));
		m_viewDataList.put(ROOT_VIEW_KEY, vd);

		// 2
		vd = new ViewData();
		vd.addItem(new ViewItemData(dummyBitmap, "Back", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Loadout 1", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Loadout 2", null, null));
		m_viewDataList.put("view_loadout", vd);