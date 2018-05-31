using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Threading.Tasks;

//use this upload one dir all files to servers...
class TransferFileName
{

    private static readonly HttpClient client = new HttpClient();

    public static void Main()
    {

        var values = getFileName("d:\\git");

        var content = new FormUrlEncodedContent(values);

        string a = PostAct("http://virtual_qq.com/p.php", content);

        Console.WriteLine(a);

        Console.ReadLine();
    }

    public static string PostAct(string url, FormUrlEncodedContent xmlString)

    {

        HttpClient httpClient = new HttpClient();

        HttpResponseMessage response = httpClient.PostAsync(url, xmlString).Result;

        if (response.IsSuccessStatusCode)
        {
            Task<string> t = response.Content.ReadAsStringAsync();
            return t.Result;
        }
        return string.Empty;
    }

    public static Dictionary<string,string> getFileName(string dir)
    {
        try
        {
            return ListFiles("",new DirectoryInfo(dir));
        }
        catch (IOException e)
        {   
            var values = new Dictionary<string, string>
            {
               { "status", "500" },
               { "msg", "file is unaccessable..." }
            };
            return values;
        }
    }

    //只列出第一级文件夹下的所有文件名
    public static Dictionary<string,string> ListFiles(string fileName,FileSystemInfo info)
    {
        Dictionary<string, string> rmsg;
        if (!info.Exists)
        {
            rmsg = new Dictionary<string, string>
            {
               { "status", "501" },
               { "msg", "dir is wrong ..." }
            };
            return rmsg;

        }

        DirectoryInfo dir = info as DirectoryInfo;
        //不是目录 
        if (dir == null)
        {

           rmsg = new Dictionary<string, string>
            {
               { "status", "502" },
               { "msg", "dir is null ..." }
            };
            return rmsg;

        }

        FileSystemInfo[] files = dir.GetFileSystemInfos();

        for (int i = 0; i < files.Length; i++)
        {

            fileName += files[i] + ",";

        }

        rmsg = new Dictionary<string, string>
            {
               { "status", "200" },
               { "msg", fileName },
            };

        return rmsg;

    }
}