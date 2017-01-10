package com.gameinbucket.render;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GeometryBuffer 
{
	private ShortBuffer index_buffer;
	private FloatBuffer vertex_buffer;

	private Vertices vertex_order;
	private Indices index_order;

	public GeometryBuffer(int vertex_limit, int index_limit)
	{
		vertex_limit *= Vertex.stride;

		ByteBuffer vbb = ByteBuffer.allocateDirect(Vertex.floatsize * vertex_limit);
		vbb.order(ByteOrder.nativeOrder());
		vertex_buffer = vbb.asFloatBuffer();

		ByteBuffer ibb = ByteBuffer.allocateDirect(Vertex.shortsize * index_limit);
		ibb.order(ByteOrder.nativeOrder());
		index_buffer = ibb.asShortBuffer();

		vertex_order = new Vertices(vertex_limit);
		index_order = new Indices(index_limit);
	}

	public void begin()
	{
		vertex_order.clear();
		index_order.clear();
	}

	public void add(float[] vertices, int indices)
	{
		vertex_order.add(vertices);
		index_order.add(indices);
	}

	public void end()
	{
		vertex_buffer.clear();
		vertex_buffer.put(vertex_order.vertices, 0, vertex_order.position);
		vertex_buffer.position(0);

		index_buffer.clear();
		index_buffer.put(index_order.indices, 0, index_order.position);
		index_buffer.position(0);
	}

	//index count
	public short ic()
	{
		return index_order.position;
	}

	//vertex buffer position
	public Buffer vbp(int p)
	{
		return vertex_buffer.position(p);
	}

	//index buffer
	public ShortBuffer ib()
	{
		return index_buffer;
	}
}
