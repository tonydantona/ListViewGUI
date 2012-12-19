package com.ups.orion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;

public class JsonStopsReader
{
	public List<Stop> readJsonStream(InputStream in) throws IOException
	{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try
		{
			return readStops(reader);
		}
		finally
		{
			reader.close();
		}
	}

	public List<Stop> readStops(JsonReader reader) throws IOException
	{
		List<Stop> stops = new ArrayList<Stop>();

		reader.beginArray();

		while (reader.hasNext())
		{
			stops.add(readStop(reader));
		}

		reader.endArray();
		return stops;
	}

	public Stop readStop(JsonReader reader) throws IOException
	{

		Stop stop = new Stop();

		reader.beginObject();
		while (reader.hasNext())
		{
			String name = reader.nextName();
			if (name.equals("stopid"))
			{
				stop.stopid = reader.nextInt();
			}
			else if (name.equals("address"))
			{
				stop.address = reader.nextString();
			}
			else if (name.equals("lat"))
			{
				stop.lat = reader.nextDouble();
			}
			else if (name.equals("lon"))
			{
				stop.lon = reader.nextDouble();
			}
			else if (name.equals("svctime"))
			{
				stop.svctime = reader.nextDouble();
			}
			else if (name.equals("numpkgs"))
			{
				stop.numpkgs = reader.nextInt();
			}
			else if (name.equals("svc"))
			{
				stop.svc = reader.nextString();
			}
			else if (name.equals("rdo"))
			{
				stop.rdo = reader.nextInt();
			}
			else if (name.equals("odo"))
			{
				stop.odo = reader.nextInt();
			}
			else if (name.equals("esstclicks"))
			{
				stop.esstclicks = reader.nextInt();
			}
			else if (name.equals("lsstclicks"))
			{
				stop.lsstclicks = reader.nextInt();
			}
			else if (name.equals("eta"))
			{
				stop.eta = reader.nextInt();
			}
			else if (name.equals("packages"))
			{
				// the pkgs in the json file are an array (specifically an arary of pkg objects) so we must use beginArray then read each object
				reader.beginArray();
				while( reader.hasNext() )
				{
					stop.packages.add(readPackage(reader));
				}
				
				reader.endArray();
			}
			else
			{
				reader.skipValue();
			}
		}

		reader.endObject();

		return stop;
	}

	public Package readPackage(JsonReader reader) throws IOException
	{
		/**
		 * how to check for null:  else if (name.equals("geo") && reader.peek() != JsonToken.NULL)
		 */
		
		/**
		 * right now this only reads one package
		 */

		Package pkg = new Package();

		reader.beginObject();
		
		while (reader.hasNext())
		{
			String name = reader.nextName();

			if (name.equals("consignee"))
			{
				pkg.consignee = reader.nextString();
			}
			else if (name.equals("earliest"))
			{
				// may have to put some code here to convert from string to int
				pkg.earliest = reader.nextInt();
			}
			else if (name.equals("commit"))
			{
				// may have to put some code here to convert from string to int
				pkg.commit = reader.nextInt();
			}
			else if (name.equals("svclevel"))
			{
				// may have to put some code here to convert from string to int
				pkg.svclevel = reader.nextInt();
			}
			else
			{
				reader.skipValue();
			}
		}

		reader.endObject();

		return pkg;
	}
}
