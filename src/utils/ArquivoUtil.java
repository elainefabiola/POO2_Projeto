
package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {
	public static <T> void salvarLista(String arquivo, List<T> lista) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
			oos.writeObject(lista);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> lerLista(String arquivo) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
			return (List<T>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return new ArrayList<>();
		}
	}
}
